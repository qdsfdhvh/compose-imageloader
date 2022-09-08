echo $SIGNING_KEY | base64 -d > ./key.gpg
echo "signing.keyId=$SIGNING_KEY_ID
signing.password=$SIGNING_KEY_PASSWORD
signing.secretKeyRingFile=./key.gpg
mavenCentralUsername=$NEXUS_USERNAME
mavenCentralPassword=$NEXUS_PASSWORD" >publish.properties