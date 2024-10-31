import React, { useState, useEffect } from 'react';
import { request, setAuthHeader, getDataFromToken } from './helpers/axios_helper';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Container } from '@mui/material';
// import Buttons from './Buttons';
// import WelcomeContent from './WelcomeContent';
import LoginForm from './LoginForm';
import AuthContent from './AuthContent';
import EventList from './EventList';
import TopBar from './TopBar';

const AppContent = () => {
    const [componentToShow, setComponentToShow] = useState("login");
    const [username, setUsername] = useState(null);

    useEffect(() => {
        const savedUsername = localStorage.getItem("username");
        const token = localStorage.getItem("auth_token");
        console.log("Token:", token, componentToShow);

        




        if (token) {
            const tokenData = getDataFromToken();
            console.log("Token data:", tokenData);

            if (tokenData.exp < Date.now() / 1000) {
                console.log("Token wygasł");
                setAuthHeader(null);
                setComponentToShow("login");
                setUsername(tokenData.login);
                return;
            }

            setAuthHeader(token);
            setComponentToShow("messages");
            setUsername(savedUsername);
        }
        else {
            setComponentToShow("login");
            setUsername(savedUsername);
        }
    }, []);


    // const login = () => {
    //     setComponentToShow("login");
    // };

    const isLoggedIn = () => {
        return localStorage.getItem("auth_token") !== null;
    }


    const onLogout = () => {
        setComponentToShow("login");
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
            setComponentToShow("messages");
        }).catch((error) => {
            setAuthHeader(null);
            setComponentToShow("welcome");
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
            setComponentToShow("messages");
        }).catch((error) => {
            setAuthHeader(null);
            setComponentToShow("welcome");
        });
    };

    return (
        <Container
            maxWidth={false}  // Wyłącz domyślną szerokość
            sx={{
                maxWidth: '1286px',
                mx: 'auto', 
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                padding: {
                    xs: '0 16px', // Małe paddingi dla małych ekranów
                    sm: '0 24px', // Większe dla ekranów sm
                    md: '0 32px', // Jeszcze większe dla md
                    lg: '0 40px', // Większe dla lg
                    xl: '0 48px', // Największe dla xl
                },
            }}
        >
            <TopBar
                isLoggedIn={isLoggedIn()}
                userName={localStorage.getItem("username")}
                onLogout={onLogout}
                onSettings={() => setComponentToShow("settings")}
            />

            {componentToShow === "login" && <LoginForm onLogin={onLogin} onRegister={onRegister} />}
            {componentToShow === "messages" && (
                <div>
                    <h1>Antares</h1>
                    <p>Witaj, {username}!</p>
                    <button onClick={onLogout}>Wyloguj</button>
                    <EventList />
                </div>
            )}


            {/* {componentToShow === "messages" && <Messages />}
            {componentToShow === "welcome" && <Welcome />} */}
            {/* {componentToShow === "settings" && <Settings />} */}
        </Container>
    );
};

export default AppContent;
