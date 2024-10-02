import React, {useState, useEffect} from 'react';
import {View, Text, Button, StyleSheet, TextInput} from 'react-native';
import {useRoute} from '@react-navigation/native';

const GeoFence = () => {
  const route = useRoute(); // This will give you access to the navigation params
  const [couponCode, setCouponCode] = useState(''); // State for storing coupon code

  // Retrieve coupon code from params and autofill it in the text box
  useEffect(() => {
    if (route.params?.coupon_code) {
      setCouponCode(route.params.coupon_code); // Set the coupon code from params
    }
  }, [route.params?.coupon_code]);

  const handlePress = () => {
    alert(`Button Pressed with Coupon Code: ${couponCode}`);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Welcome to the GeoFence Screen</Text>

      {/* Display coupon code if it exists */}
      {couponCode ? (
        <Text style={styles.couponText}>Your Coupon Code: {couponCode}</Text>
      ) : (
        <Text style={styles.noCouponText}>No Coupon Code Found</Text>
      )}

      {/* Text Input for displaying the coupon code */}
      <TextInput
        style={styles.input}
        placeholder="Enter Coupon Code"
        value={couponCode} // Autofill the coupon code
        onChangeText={setCouponCode} // Update the coupon code when the user types
      />

      <Button title="Use Coupon" onPress={handlePress} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f8f9fa',
  },
  title: {
    fontSize: 24,
    marginBottom: 20,
    fontWeight: 'bold',
    color: '#333',
  },
  input: {
    height: 40,
    borderColor: '#ccc',
    borderWidth: 1,
    marginBottom: 20,
    paddingHorizontal: 10,
    width: '80%',
  },
  couponText: {
    fontSize: 18,
    marginBottom: 20,
    color: 'green',
  },
  noCouponText: {
    fontSize: 18,
    marginBottom: 20,
    color: 'red',
  },
});

export default GeoFence;
