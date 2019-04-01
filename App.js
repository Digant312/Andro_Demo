import React, { Component } from "react";
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  Image
} from "react-native";
import ToastExample from "./ToastExample";
import ImageCropper from "./ImageCropper";
import ImagePicker from "react-native-image-picker";

const imagePath = require("./error.png");

export default class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      data: "Hello",
      imagePath: "",
      base64Image:""
    };
  }

  async showToastPromise(uri) {
    try {
      ToastExample.show(uri, 300).then(data => {
        console.log("Promise done", data);
        this.setState({
          profile_image: data.profile_image,
          avatar_image: data.avatar_image
        });
      });
    } catch (e) {
      this.setState = {
        data: "Error"
      };

      console.error('error: ',e);
    }
  }

  selectPhotoTapped() {
    const options = {
      quality: 1.0,
      maxWidth: 500,
      maxHeight: 500,
      storageOptions: {
        skipBackup: true
      },
      mediaType: 'photo',
      title: '',
      takePhotoButtonTitle: null,
      chooseFromLibraryButtonTitle: 'Select Profile Photo',
    };

    ImagePicker.showImagePicker(options, response => {

      if (response.didCancel) {
        console.log("User cancelled photo picker");
      } else if (response.error) {
        console.log("ImagePicker Error: ", response.error);
      } else if (response.customButton) {
        console.log("User tapped custom button: ", response.customButton);
      } else {
        let source = { uri: response.uri };

        // You can also display the image using data:
        // let source = { uri: 'data:image/jpeg;base64,' + response.data };

        this.showToastPromise(response.uri)

        this.setState({
          imagePath: source
        });
      }
    });
  }

  render() {
    console.log('this.state.base64Image', this.state.base64Image)
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={() => this.selectPhotoTapped()}>
        <Text style={styles.instructions}>Select Image</Text>
        </TouchableOpacity>
        {this.state.avatar_image ?
        <Image source={{ isStatic:true, uri: this.state.avatar_image }} style={{width: 150, height: 150, borderRadius: 100}}/>
        : null}
        {this.state.profile_image ?
        <Image source={{ isStatic:true, uri: this.state.profile_image }} style={{width: 200, height: 150}}/>
        : null}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#fff"
  },
  instructions: {
    textAlign: "center",
    color: "#333333",
    marginBottom: 5,
    fontFamily: 'Bogle-Bold'
  }
});
