// import React, {useState, useEffect} from 'react';
// import {View, Text, Button, StyleSheet, TextInput} from 'react-native';
// import {useRoute} from '@react-navigation/native';

// const Login = () => {
//   const route = useRoute(); // This will give you access to the navigation params
//   const [couponCode, setCouponCode] = useState(''); // State for storing coupon code

//   // Retrieve coupon code from params and autofill it in the text box
//   useEffect(() => {
//     if (route.params?.coupon_code) {
//       setCouponCode(route.params.coupon_code); // Set the coupon code from params
//     }
//   }, [route.params?.coupon_code]);

//   const handlePress = () => {
//     alert(`Button Pressed with Coupon Code: ${couponCode}`);
//   };

//   return (
//     <View style={styles.container}>
//       <Text style={styles.title}>Welcome to the Login Screen</Text>

//       {/* Display coupon code if it exists */}
//       {couponCode ? (
//         <Text style={styles.couponText}>Your Coupon Code: {couponCode}</Text>
//       ) : (
//         <Text style={styles.noCouponText}>No Coupon Code Found</Text>
//       )}

//       {/* Text Input for displaying the coupon code */}
//       <TextInput
//         style={styles.input}
//         placeholder="Enter Coupon Code"
//         value={couponCode} // Autofill the coupon code
//         onChangeText={setCouponCode} // Update the coupon code when the user types
//       />

//       <Button title="Use Coupon" onPress={handlePress} />
//     </View>
//   );
// };

// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     justifyContent: 'center',
//     alignItems: 'center',
//     backgroundColor: '#f8f9fa',
//   },
//   title: {
//     fontSize: 24,
//     marginBottom: 20,
//     fontWeight: 'bold',
//     color: '#333',
//   },
//   input: {
//     height: 40,
//     borderColor: '#ccc',
//     borderWidth: 1,
//     marginBottom: 20,
//     paddingHorizontal: 10,
//     width: '80%',
//   },
//   couponText: {
//     fontSize: 18,
//     marginBottom: 20,
//     color: 'green',
//   },
//   noCouponText: {
//     fontSize: 18,
//     marginBottom: 20,
//     color: 'red',
//   },
// });

// export default Login;

// Login.js
import React, {useState} from 'react';
import {
  View,
  TextInput,
  Button,
  StyleSheet,
  Alert,
  TouchableOpacity,
  Text,
} from 'react-native';

const CleverTap = require('clevertap-react-native');

const Login = ({navigation}) => {
  CleverTap.setDebugLevel(3);
  CleverTap.initializeInbox();
  const [inputValues, setInputValues] = useState({
    name: '',
    mobileNumber: '',
    identity: '',
    email: '',
    password: '',
  });

  const handleInputChange = (name, value) => {
    // If the input is for mobile number, ensure it's only 10 digits
    if (name === 'mobileNumber') {
      // Remove non-digit characters
      const digitsOnly = value.replace(/[^0-9]/g, '');
      // Allow only the first 10 digits
      setInputValues(prevState => ({
        ...prevState,
        mobileNumber: digitsOnly.slice(0, 10),
      }));
    } else {
      setInputValues(prevState => ({
        ...prevState,
        [name]: value,
      }));
    }
  };

  const handleSubmit = () => {
    // Log all the values entered in the input text
    console.log(inputValues);

    // Optionally, you can show an alert with the input values
    Alert.alert('Logged Values', JSON.stringify(inputValues));

    // Redirect to Home Page
    navigation.navigate('Home');

    CleverTap.onUserLogin({
      Name: inputValues.name,
      Identity: inputValues.identity,
      Email: inputValues.email,
      Phone: '+91' + inputValues.mobileNumber,
      disablePromotionalNoti: 'no',
      'MSG-email': true,
      'MSG-push': true,
      'MSG-sms': true,
      'MSG-whatsapp': true,
    });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Login</Text>
      <TextInput
        style={styles.input}
        placeholder="Name"
        value={inputValues.name}
        onChangeText={value => handleInputChange('name', value)}
      />
      <TextInput
        style={styles.input}
        placeholder="Mobile Number"
        keyboardType="phone-pad"
        value={inputValues.mobileNumber}
        onChangeText={value => handleInputChange('mobileNumber', value)}
      />
      <TextInput
        style={styles.input}
        placeholder="Identity"
        value={inputValues.identity}
        onChangeText={value => handleInputChange('identity', value)}
      />
      <TextInput
        style={styles.input}
        placeholder="Email ID"
        keyboardType="email-address"
        value={inputValues.email}
        onChangeText={value => handleInputChange('email', value)}
      />
      <TextInput
        style={styles.input}
        placeholder="Password"
        secureTextEntry // This masks the input
        value={inputValues.password}
        onChangeText={value => handleInputChange('password', value)}
      />
      <TouchableOpacity style={styles.button} onPress={handleSubmit}>
        <Text style={styles.buttonText}>Login</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 16,
  },
  input: {
    height: 50,
    borderColor: 'gray',
    borderWidth: 1,
    borderRadius: 25, // Make the input oval-shaped
    marginBottom: 10, // Margin between inputs
    paddingLeft: 16,
    paddingRight: 16,
    backgroundColor: '#fff', // Optional: Add a background color
  },
  button: {
    height: 50,
    borderRadius: 25, // Make the button oval-shaped
    backgroundColor: '#2196f3', // Button color
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 10, // Margin between inputs and button
  },
  buttonText: {
    color: '#fff', // Text color
    fontSize: 18,
  },
  title: {
    fontSize: 25,
    fontWeight: 'bold',
    textTransform: 'uppercase',
    textAlign: 'center',
    paddingVertical: 20,
    color: '#2196f3',
  },
});

export default Login;
