import React, { useState } from 'react';
import { request, setAuthHeader } from './helpers/axios_helper';
// import Buttons from './Buttons';
// import WelcomeContent from './WelcomeContent';
import LoginForm from './LoginForm';
import AuthContent from './AuthContent';

const AppContent = () => {
    const [componentToShow, setComponentToShow] = useState("login");

    const login = () => {
        setComponentToShow("login");
    };

    const logout = () => {
        setComponentToShow("welcome");
        setAuthHeader(null);
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
            <div>
                <h1>Antares</h1>
            </div>
            {/* <Buttons login={login} logout={logout} /> */}
            {/* {componentToShow === "welcome" && <WelcomeContent />} */}
            {componentToShow === "login" && <LoginForm onLogin={onLogin} onRegister={onRegister} />}
            {/* {componentToShow === "messages" && <AuthContent />} */}
        </>
    );
};

export default AppContent;
