import React, { useState } from 'react';
import { Box, Tabs, Tab, TextField, Button, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const LoginForm = ({ onLogin, onRegister }) => {
    const [activeTab, setActiveTab] = useState("login");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    const onChangeHandler = (event) => {
        const { name, value } = event.target;
        switch (name) {
            case "firstName":
                setFirstName(value);
                break;
            case "lastName":
                setLastName(value);
                break;
            case "login":
                setLogin(value);
                break;
            case "password":
                setPassword(value);
                break;
            default:
                break;
        }
    };

    const onSubmitLogin = (e) => {
        e.preventDefault();
        onLogin(e, login, password);
        navigate("/");
    };

    const onSubmitRegister = (e) => {
        e.preventDefault();
        onRegister(e, firstName, lastName, login, password);
        navigate("/");
    };

    return (
        <Box sx={{ width: '100%', maxWidth: 400, mx: 'auto', mt: 4 }}>
            <Tabs
                value={activeTab}
                onChange={(e, newValue) => setActiveTab(newValue)}
                centered
                sx={{ mb: 3 }}
            >
                <Tab value="login" label="Login" />
                <Tab value="register" label="Register" />
            </Tabs>

            {activeTab === "login" && (
                <Box component="form" onSubmit={onSubmitLogin} noValidate sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                    <TextField
                        label="Username"
                        variant="outlined"
                        name="login"
                        value={login}
                        onChange={onChangeHandler}
                        fullWidth
                    />
                    <TextField
                        label="Password"
                        type="password"
                        variant="outlined"
                        name="password"
                        value={password}
                        onChange={onChangeHandler}
                        fullWidth
                    />
                    <Button type="submit" variant="contained" color="primary" fullWidth>
                        Sign in
                    </Button>
                </Box>
            )}

            {activeTab === "register" && (
                <Box component="form" onSubmit={onSubmitRegister} noValidate sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                    <TextField
                        label="First Name"
                        variant="outlined"
                        name="firstName"
                        value={firstName}
                        onChange={onChangeHandler}
                        fullWidth
                    />
                    <TextField
                        label="Last Name"
                        variant="outlined"
                        name="lastName"
                        value={lastName}
                        onChange={onChangeHandler}
                        fullWidth
                    />
                    <TextField
                        label="Username"
                        variant="outlined"
                        name="login"
                        value={login}
                        onChange={onChangeHandler}
                        fullWidth
                    />
                    <TextField
                        label="Password"
                        type="password"
                        variant="outlined"
                        name="password"
                        value={password}
                        onChange={onChangeHandler}
                        fullWidth
                    />
                    <Button type="submit" variant="contained" color="primary" fullWidth>
                        Sign up
                    </Button>
                </Box>
            )}
        </Box>
    );
};

export default LoginForm;
