import React from 'react';
import {
  Linking,
  SafeAreaView,
  StyleSheet,
  View,
  ToastAndroid,
  AppState,
} from 'react-native';
import {Component} from 'react';
import {Button, Alert, Text} from 'react-native';

const CleverTap = require('clevertap-react-native');

const Separator = () => <View style={styles.separator} />;

CleverTap.addListener(CleverTap.CleverTapPushNotificationClicked, event => {
  console.log(
    'handleCleverTapEvent',
    CleverTap.CleverTapPushNotificationClicked,
    event,
  );
});

CleverTap.addListener(CleverTap.CleverTapInboxDidInitialize, event => {
  _handleCleverTapInbox(CleverTap.CleverTapInboxDidInitialize, event);
});

CleverTap.addListener(CleverTap.CleverTapInboxMessageTapped, event => {
  var abc = JSON.stringify(event);
  console.log('CleverTapInboxMessageTapped: ', abc);
});

function _handleCleverTapInbox(eventName, event) {
  console.log('handleCleverTapInbox', eventName, event);
  ToastAndroid.show(`${eventName} called!`, ToastAndroid.SHORT);
}

class App extends Component {
  constructor() {
    super();
    CleverTap.setDebugLevel(3);
    CleverTap.initializeInbox();
  }

  state = {
    title: 'Title',
    message: 'Message',
    appState: AppState.currentState,
  };

  //to add the listener
  componentDidMount() {
    AppState.addEventListener('change', this._handleAppStateChange);
  }

  //to remove the listener
  componentWillUnmount() {
    AppState.removeEventListener('change', this._handleAppStateChange);
  }

  //To fetch the app state
  _handleAppStateChange = nextAppState => {
    this.setState({appState: nextAppState});

    console.log('AppState' + this.state.appState);
    if (this.state.appState === 'background') {
      //for background
      console.log('Background Mode.');
      CleverTap.discardInAppNotifications();
    } else if (this.state.appState === 'active') {
      //for foreground
      console.log('Foreground Mode.');
    } else if (this.state.appState === 'inactive') {
      //for inactive
      console.log('inactive Mode.');
    }
  };

  updateProfile = () => {
    CleverTap.profileSet({
      DOB: new Date('1996-10-11T06:35:31'),
      Photo: 'https://www.entforall.com/images/founder.jpg',
    });
    Alert.alert('Profile Update Clicked');
  };

  pushEvent = () => {
    Alert.alert('Push Event Clicked');
    CleverTap.recordEvent('Product Viewed');
  };

  pushNotificationn = () => {
    Alert.alert('Push Notification Clicked');
    CleverTap.recordEvent('Karthiks Noti Event');
  };

  reactPush = () => {
    Alert.alert('React Push Notification Clicked');
    CleverTap.recordEvent('Karthiks React Noti Event');
  };

  reactKKPush = () => {
    Alert.alert('React Push Notification Clicked');
    // CleverTap.recordEvent('KarthikNotiEventNew');
    CleverTap.recordEvent('Test Sharif');
  };

  inApp = () => {
    Alert.alert('In App Clicked');
    CleverTap.recordEvent('Karthiks InApp Event');
  };

  //App Inbox
  appInbox = (eventName, event) => {
    console.log('CleverTap Inbox Event - ', eventName, event);
    CleverTap.recordEvent('Karthiks App Inbox Event');
    CleverTap.showInbox({
      tabs: ['Activites', 'Announcements'],
      navBarTitle: 'My App Inbox',
      navBarTitleColor: '#000000',
      navBarColor: '#FFFFFF',
      inboxBackgroundColor: '#AED6F1',
      backButtonColor: '#000000',
      noMessageText: 'No message(s)',
      noMessageTextColor: '#FF0000',
      unselectedTabColor: '#0000FF',
      selectedTabColor: '#FF0000',
      selectedTabIndicatorColor: '#000000',
      firstTabTitle: 'Activities',
    });

    CleverTap.getAllInboxMessages((err, res) => {
      console.log('All Inbox Messages: ', res, err);
      console.log('LOGGGG: ', res.length);
    });

    Alert.alert('App Inbox Clicked');
  };

  nativeDisplay = () => {
    CleverTap.recordEvent('Karthiks Native Display Event');
    CleverTap.getAllDisplayUnits((err, res) => {
      console.log('All Display Units: ', res, err);
      this.setState({datasource: JSON.parse(res)});
      this.setState({
        title: JSON.stringify(
          this.state.datasource.content[0].title.text,
        ).replace(/['"]+/g, ''),
        message: JSON.stringify(
          this.state.datasource.content[0].message.text,
        ).replace(/['"]+/g, ''),
      });
      Alert.alert('Native Display Clicked');

      // for Display Unit Id use the below one
      CleverTap.pushDisplayUnitViewedEventForID(this.state.datasource.wzrk_id);
      CleverTap.pushDisplayUnitClickedEventForID(this.state.datasource.wzrk_id);
    });
  };

  render() {
    return (
      <SafeAreaView style={styles.container}>
        <View>
          <Button title="Update Profile" onPress={this.updateProfile} />
          <Separator />
          <Button title="Push Event" onPress={this.pushEvent} />
          <Separator />
          <Button title="Push Notification" onPress={this.pushNotificationn} />
          <Separator />
          <Button title="In App" onPress={this.inApp} />
          <Separator />
          <Button title="App Inbox" onPress={this.appInbox} />
          <Separator />
          <Button title="Native Display" onPress={this.nativeDisplay} />
          <Separator />
          <Button title="React PN" onPress={this.reactPush} />
          <Separator />
          <Button title="React KK PN" onPress={this.reactKKPush} />
          <Separator />
          <Text style={styles.titleText}>Native Display Message </Text>
          <Separator />
          <Text style={styles.textND}>{this.state.title}</Text>
          <Separator />
          <Text style={styles.textND}>{this.state.message}</Text>
        </View>
      </SafeAreaView>
    );
  }
}

CleverTap.onUserLogin({
  Name: 'Karthik',
  Identity: 'test84',
  Email: 'test84@test.com',
  Phone: '+91123456789',
  Gender: 'M',
  DOB: new Date(),
  'MSG-email': true,
  'MSG-push': true,
  'MSG-sms': true,
  'MSG-whatsapp': true,
});

CleverTap.createNotificationChannel(
  'testkk123',
  'React Native Test',
  'This is a React-Native test by Karthik',
  5,
  true,
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    marginHorizontal: 16,
  },
  item: {
    backgroundColor: 'teal',
  },
  separator: {
    marginVertical: 8,
    borderBottomColor: '#737373',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  titleText: {
    fontSize: 20,
    fontWeight: 'bold',
    color: 'black',
  },
  textND: {
    fontSize: 18,
    color: 'black',
  },
});

export default App;
