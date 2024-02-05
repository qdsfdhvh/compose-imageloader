package com.seiko.imageloader.demo.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.demo.util.JSON
import com.seiko.imageloader.demo.util.httpEngine
import com.seiko.imageloader.ui.AutoSizeImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Composable
fun WanAndroidScene(
    onBack: () -> Unit,
) {
    BackScene(
        onBack = onBack,
        title = { Text("WanAndroid") },
    ) { innerPadding ->
        val client by remember {
            lazy {
                HttpClient(httpEngine) {
                    install(ContentNegotiation) {
                        json(JSON)
                    }
                    defaultRequest {
                        url("https://www.wanandroid.com/")
                    }
                }
            }
        }
        Column(Modifier.padding(innerPadding).fillMaxSize()) {
            val tabs by produceState(emptyList()) {
                value = runCatching {
                    client.get("project/tree/json")
                        .wanBody<List<WanProjectTabs>>()
                        .take(5)
                }.getOrElse {
                    it.printStackTrace()
                    emptyList()
                }
            }
            if (tabs.isEmpty()) return@Column

            var selectedTabIndex by remember { mutableIntStateOf(0) }
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                tabs = {
                    tabs.forEachIndexed { index, tag ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = { selectedTabIndex = index },
                            modifier = Modifier.widthIn(min = 100.dp),
                        ) {
                            Text(tag.name)
                        }
                    }
                },
            )

            val projects by produceState(emptyList(), key1 = tabs, key2 = selectedTabIndex) {
                value = runCatching {
                    client.get("project/list/1/json?cid=${tabs[selectedTabIndex].id}")
                        .wanBody<WanProjectPage>().projects
                }.getOrElse {
                    it.printStackTrace()
                    emptyList()
                }
            }

            println(projects.firstOrNull()?.envelopePic ?: "null")

            LazyVerticalGrid(
                GridCells.Fixed(3),
                Modifier.weight(1f).fillMaxWidth(),
            ) {
                items(
                    projects,
                    key = { it.id },
                ) { project ->
                    Column {
                        AutoSizeImage(
                            project.envelopePic,
                            contentDescription = project.title,
                            modifier = Modifier.size(200.dp),
                        )
                        Text(project.title)
                    }
                }
            }
        }
    }
}

private suspend inline fun <reified T> HttpResponse.wanBody(): T {
    val response = body<WanResponse<T>>()
    if (response.errorCode == 0) {
        return response.data
    } else {
        error(response.errorMsg)
    }
}

@Serializable
private data class WanResponse<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String,
)

@Serializable
private data class WanProjectTabs(
    val id: Int,
    val name: String,
)

@Serializable
private data class WanProjectPage(
    val curPage: Int,
    @SerialName("datas") val projects: List<WanProject>,
)

@Serializable
private data class WanProject(
    val id: Int,
    val title: String,
    val envelopePic: String,
)
