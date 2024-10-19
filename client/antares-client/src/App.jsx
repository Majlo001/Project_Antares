import React, { useState, useEffect } from 'react';
import { request, setAuthHeader } from './helpers/axios_helper';
// import Buttons from './Buttons';
// import WelcomeContent from './WelcomeContent';
import LoginForm from './LoginForm';
import AuthContent from './AuthContent';
import EventList from './EventList';

const AppContent = () => {
    const [componentToShow, setComponentToShow] = useState("login");
    const [username, setUsername] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("auth_token");
        console.log("Token:", token, componentToShow);
        const savedUsername = localStorage.getItem("username");

        if (token) {
            setAuthHeader(token);
            setComponentToShow("messages");
            setUsername(savedUsername);
        }
    }, []);


    const login = () => {
        setComponentToShow("login");
    };

    const logout = () => {
        setComponentToShow("welcome");
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
            setAuthHeader(response.data.token);
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
            setComponentToShow("messages");
        }).catch((error) => {
            setAuthHeader(null);
            setComponentToShow("welcome");
        });
    };

    return (
        <>
            {componentToShow === "messages" && (
                <div>
                    <h1>Antares</h1>
                    <p>Witaj, {username}!</p> {/* Wyświetlenie loginu użytkownika */}
                    <button onClick={logout}>Wyloguj</button>
                    <EventList />
                </div>
            )}
            {componentToShow === "login" && <LoginForm onLogin={onLogin} onRegister={onRegister} />}
            {/* <Buttons login={login} logout={logout} /> */}
            {/* {componentToShow === "welcome" && <WelcomeContent />} */}
            {/* {componentToShow === "messages" && <AuthContent />} */}
        </>
    );
};

export default AppContent;
