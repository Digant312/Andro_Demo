import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import ToastExample from './ToastExample';

export default class App extends Component{
  constructor(props){
    super(props);

    this.state = {
      showToast: false
    }
  }

  showToast = () => {
    this.setState({ showToast : true })
  }

  render() {
    console.log(ToastExample)
    return (
      <View style={styles.container}>        
        <TouchableOpacity onPress={() => this.showToast()}>
          <Text style={styles.instructions}>SHOW TOAST</Text>
        </TouchableOpacity>
        {this.state.showToast ?
          ToastExample.show(`Welcome to Android World!`, ToastExample.LONG)
        : null }
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#fff',
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
