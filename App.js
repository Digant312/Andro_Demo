import React, { Component } from "react";
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
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
      imagePath: ""
    };
  }

  async showToastPromise(uri) {
    try {
      console.log(imagePath);
      ToastExample.show(uri, 300).then(data => {
        console.log("Promise done");
        this.state = {
          data: data
        };
        console.log(this.state.data);
      });
    } catch (e) {
      this.setState = {
        data: "Error"
      };

      console.error(e);
    }
  }

  selectPhotoTapped() {
    const options = {
      quality: 1.0,
      maxWidth: 500,
      maxHeight: 500,
      storageOptions: {
        skipBackup: true
      }
    };

    ImagePicker.showImagePicker(options, response => {
      console.log("Response = ", response);

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
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={() => this.selectPhotoTapped()}>
        <Text style={styles.instructions}>Select Image</Text>
        </TouchableOpacity>
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
