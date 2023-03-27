//
//  ContentView.swift
//  iosApp
//
//  Created by Seiko on 3/27/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import UIKit
import SwiftUI
import combine

struct ComposeView : UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        return MainKt.MainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
    }
}

struct ContentView : View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard)
    }
}
