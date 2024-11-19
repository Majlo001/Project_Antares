import React, { createContext, useState, useEffect } from 'react';

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cartData, setCartData] = useState({});

    useEffect(() => {
        console.log("CartContext: cartData:", cartData);
    }, [cartData]);

    useEffect(() => {
        const sessionCart = sessionStorage.getItem('selectedSeatsMap');
        if (sessionCart) {
            setCartData(JSON.parse(sessionCart));
        }
    }, []); 

    const clearCart = () => {
        setCartData({});
        sessionStorage.setItem('selectedSeatsMap', JSON.stringify({}));
    };

    const updateCart = (eventId, updatedSeats) => {
        const updatedCart = { ...cartData, [eventId]: updatedSeats };
        setCartData(updatedCart);
    };

    const calculateCartItems = () => {
        return Object.values(cartData).reduce((total, nestedArrays) => {
            if (!nestedArrays) {
                return total;
            }
            return total + nestedArrays.reduce((sum, array) => sum + array.length, 0);
        }, 0);
    };

    return (
        <CartContext.Provider value={{ cartData, updateCart, clearCart, calculateCartItems }}>
            {children}
        </CartContext.Provider>
    );
};
