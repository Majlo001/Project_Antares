import React, { useState, useEffect } from 'react';
import { request, setAuthHeader, getDataFromToken } from './helpers/axios_helper';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Container } from '@mui/material';
import LoginForm from './LoginForm';
import AuthContent from './AuthContent';
import MainPage from './MainPage';
import TopBar from './TopBar';
import Cart from './Cart';
import EventDetail from './EventDetail';
import CreateEventForm from './creationForms/CreateEventForm';
import LocationSeatChart from './LocationSeatChart';
import PaymentSuccess from './PaymentSuccess';
import PaymentCancel from './PaymentCancel';
import { CartProvider } from './contexts/CartContext';

const AppContent = () => {
    const [username, setUsername] = useState(localStorage.getItem("username"));
    const [isLoggedIn, setIsLoggedIn] = useState(localStorage.getItem("auth_token") !== null);
    const [cartItemCount, setCartItemCount] = useState(0);


    useEffect(() => {
        const savedUsername = localStorage.getItem("username");
        const token = localStorage.getItem("auth_token");

        if (token) {
            const tokenData = getDataFromToken();
            console.log("Token data:", tokenData);

            if (tokenData.exp < Date.now() / 1000) {
                console.log("Token wygasł");
                setAuthHeader(null);
                setUsername(tokenData.login);
                return;
            }

            setAuthHeader(token);
            setUsername(savedUsername);
            setIsLoggedIn(true);
        }
        else {
            setUsername(null);
            setIsLoggedIn(false);
        }
    }, []);


    const onLogout = () => {
        setAuthHeader(null);
        localStorage.removeItem("auth_token");
        localStorage.removeItem("username");
        setUsername(null);
        setIsLoggedIn(false);
    };

    const onLogin = (e, username, password) => {
        e.preventDefault();
        request(
            "POST",
            "/login",
            {
                login: username,
                password: password
            }
        ).then((response) => {
            console.log("Zalogowano:", response.data);
            setAuthHeader(response.data.token);
            localStorage.setItem("username", username);
            setUsername(username);
            setIsLoggedIn(true);
        }).catch((error) => {
            setAuthHeader(null);
            setUsername(null);
            setIsLoggedIn(false);
        });
    };

    const onRegister = (e, firstName, lastName, username, password) => {
        e.preventDefault();
        request(
            "POST",
            "/register",
            {
                firstName: firstName,
                lastName: lastName,
                login: username,
                password: password
            }
        ).then((response) => {
            setAuthHeader(response.data.token);
            localStorage.setItem("username", username);
            setUsername(username);
            setIsLoggedIn(true);
        }).catch((error) => {
            setAuthHeader(null);
            setUsername(null);
            setIsLoggedIn(false);
        });
    };

    function calculateCartItems(cartData) {
        if (!cartData) return 0;
    
        let totalItems = 0;
    
        Object.values(cartData).forEach((nestedArrays) => {
            nestedArrays.forEach((array) => {
                totalItems += array.length;
            });
        });
    
        return totalItems;
    }

    return (
        <CartProvider>
            <Router>
                <Container
                    maxWidth={false}
                    sx={{
                        maxWidth: '1286px',
                        mx: 'auto',
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        padding: {
                            xs: '0 16px',
                            sm: '0 24px',
                            md: '0 32px',
                            lg: '0 40px',
                            xl: '0 48px',
                        },
                    }}
                >
                    <TopBar
                        isLoggedIn={isLoggedIn}
                        userName={username}
                        onLogout={onLogout}
                        onSettings={null}
                        cartItemCount={cartItemCount}
                    />
                    <Routes>
                        <Route path="/" element={<MainPage />} />
                        <Route path="/login" element={<LoginForm onLogin={onLogin} onRegister={onRegister} />} />
                        <Route path="/events/:eventId" element={<EventDetail />} />
                        <Route path="/admin/form/event" element={<CreateEventForm />} />
                        <Route path="/seat_chart/:eventId" element={<LocationSeatChart />} />
                        <Route path="/cart" element={<Cart />} />
                        <Route path="/payment/success" element={<PaymentSuccess />} />
                        <Route path="/payment/cancel" element={<PaymentCancel />} />
                    </Routes>
                </Container>
            </Router>
        </CartProvider>
    );
};

export default AppContent;
