import React, { Component } from "react";
import {
  Platform,
  StyleSheet,
  Text,
  View,
  TouchableOpacity
} from "react-native";
import ToastExample from "./ToastExample";
import ImageCropper from "./ImageCropper";

export default class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      data: "Hello"
    };
  }

  async showToastPromise() {
    try {
      console.log("Trying");
      ToastExample.show("Hello", 300).then(data => {
        console.log("Promise done")
        this.state = {
          data: data
        };
        console.log(this.state.data)
      });
    } catch (e) {
      this.setState = {
        data: "Error"
      };

      console.error(e);
    }
  }

  showToast = () => {
    ToastExample.show(`Welcome to Android World!`, 300);
  };

  render() {
    console.log("ImageCropper", ImageCropper);
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={() => this.showToastPromise()}>
          <Text style={styles.instructions}>SHOW TOAST</Text>

          <Text>{this.state.data}</Text>
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
    marginBottom: 5
  }
});
