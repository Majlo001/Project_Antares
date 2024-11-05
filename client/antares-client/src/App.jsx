import React, { useState, useEffect } from 'react';
import { request, setAuthHeader, getDataFromToken } from './helpers/axios_helper';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Container } from '@mui/material';
import LoginForm from './LoginForm';
import AuthContent from './AuthContent';
import EventList from './EventList';
import TopBar from './TopBar';
import EventDetail from './EventDetail';
import CreateEventForm from './creationForms/CreateEventForm';

const AppContent = () => {
    const [username, setUsername] = useState(null);

    useEffect(() => {
        const savedUsername = localStorage.getItem("username");
        const token = localStorage.getItem("auth_token");
        console.log("Token:", token);


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
        }
        else {
            setUsername(savedUsername);
        }
    }, []);

    const isLoggedIn = () => {
        return localStorage.getItem("auth_token") !== null;
    }


    const onLogout = () => {
        setAuthHeader(null);
        localStorage.removeItem("auth_token");
        localStorage.removeItem("username");
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
        }).catch((error) => {
            setAuthHeader(null);
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
        }).catch((error) => {
            setAuthHeader(null);
        });
    };

    return (
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
                    isLoggedIn={isLoggedIn()}
                    userName={localStorage.getItem("username")}
                    onLogout={onLogout}
                    onSettings={null}
                />
                <Routes>
                    <Route path="/" element={<EventList />} />
                    <Route path="/login" element={<LoginForm onLogin={onLogin} onRegister={onRegister} />} />
                    <Route path="/events/:eventId" element={<EventDetail />} />
                    <Route path="/admin/form/event" element={<CreateEventForm />} />
                </Routes>
            </Container>
        </Router>
    );
};

export default AppContent;
