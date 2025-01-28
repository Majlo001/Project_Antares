import React, { useState } from 'react';
import { Box, Tabs, Tab, TextField, Button, Typography, InputAdornment, IconButton } from '@mui/material';
import { Google as GoogleIcon, Facebook as FacebookIcon, GitHub as GitHubIcon } from '@mui/icons-material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const LoginForm = ({ onLogin, onRegister }) => {
    const [activeTab, setActiveTab] = useState("login");
    const [showPassword, setShowPassword] = useState(false);
    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    const onChangeHandler = (event) => {
        const { name, value } = event.target;
        switch (name) {
            case "email":
                setEmail(value);
                break;
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
        onRegister(e, email, firstName, lastName, login, password);
        navigate("/");
    };

    return (
        <Box sx={{ width: '100%', maxWidth: 480, mx: 'auto', mt: 4 }}>
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
                        type={showPassword ? "text" : "password"}
                        variant="outlined"
                        name="password"
                        value={password}
                        onChange={onChangeHandler}
                        fullWidth
                        InputProps={{
                            endAdornment: (
                                <InputAdornment position="end" sx={{ cursor: 'pointer', marginRight: '0.5rem' }}>
                                    <IconButton
                                        aria-label="toggle password visibility"
                                        onClick={() => setShowPassword(!showPassword)}
                                        edge="end"
                                    >
                                        {showPassword ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>
                            )
                        }}
                    />


                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
                        <Button
                            variant="outlined"
                            color="primary"
                            startIcon={<GoogleIcon />}
                            fullWidth
                            sx={{ mr: 1 }}
                        >
                            Google
                        </Button>
                        <Button
                            variant="outlined"
                            color="primary"
                            startIcon={<FacebookIcon />}
                            fullWidth
                            sx={{ mx: 1 }}
                        >
                            Facebook
                        </Button>
                        <Button
                            variant="outlined"
                            color="primary"
                            startIcon={<GitHubIcon />}
                            fullWidth
                            sx={{ ml: 1 }}
                        >
                            GitHub
                        </Button>
                    </Box>
                    
                    <Button type="submit" variant="contained" color="primary" fullWidth>
                        Sign in
                    </Button>

                    <Typography variant="body2" align="center">
                        <Box component="span" sx={{ color: 'text.primary' }}>
                            Forgot password? 
                        </Box>
                        <Box component="a" href="/forgot-password" style={{ textDecoration: 'none', color: 'text.secondary', paddingLeft: '0.2rem' }}>
                            Reset here
                        </Box>
                    </Typography>
                </Box>
            )}

            {activeTab === "register" && (
                <Box component="form" onSubmit={onSubmitRegister} noValidate sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                    <TextField
                        label="Email"
                        variant="outlined"
                        name="email"
                        value={email}
                        onChange={onChangeHandler}
                        fullWidth
                        // error={!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(email)}
                        // helperText={!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(email) ? "Invalid email address" : ""}
                    />
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
                        type={showPassword ? "text" : "password"}
                        variant="outlined"
                        name="password"
                        value={password}
                        onChange={onChangeHandler}
                        fullWidth
                        InputProps={{
                            endAdornment: (
                                <InputAdornment position="end" sx={{ cursor: 'pointer', marginRight: '0.5rem' }}>
                                    <IconButton
                                        aria-label="toggle password visibility"
                                        onClick={() => setShowPassword(!showPassword)}
                                        edge="end"
                                    >
                                        {showPassword ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>
                            )
                        }}
                        // error={password.length < 8 || !/[a-z]/.test(password) || !/[A-Z]/.test(password) || !/[0-9]/.test(password) || !/[!@#$%^&*]/.test(password)}
                    />
                    {/* <Typography variant="body2" color="error">
                        {password.length < 8 || 
                        !/[a-z]/.test(password) || 
                        !/[A-Z]/.test(password) || 
                        !/[0-9]/.test(password) || 
                        !/[!@#$%^&*]/.test(password) ? (
                            <>
                                Password must meet the following requirements:
                                <ul>
                                    {password.length < 8 && <li>At least 8 characters long</li>}
                                    {!/[a-z]/.test(password) && <li>Include at least one lowercase letter</li>}
                                    {!/[A-Z]/.test(password) && <li>Include at least one uppercase letter</li>}
                                    {!/[0-9]/.test(password) && <li>Include at least one number</li>}
                                    {!/[!@#$%^&*]/.test(password) && <li>Include at least one special character (!@#$%^&*)</li>}
                                </ul>
                            </>
                        ) : null}
                    </Typography> */}


                    <Button type="submit" variant="contained" color="primary" fullWidth>
                        Sign up
                    </Button>
                    {/* <Typography variant="body2" color="error" align="center" gutterBottom>
                        There was an issue with registration.<br />
                        Please check your details and try again.
                    </Typography> */}

                    <Typography variant="body2" align="center" sx = {{ mt: 2 }}>
                        or create your account with one of the following services:
                    </Typography>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                        <Button
                            variant="outlined"
                            color="primary"
                            startIcon={<GoogleIcon />}
                            fullWidth
                            sx={{ mr: 1 }}
                        >
                            Google
                        </Button>
                        <Button
                            variant="outlined"
                            color="primary"
                            startIcon={<FacebookIcon />}
                            fullWidth
                            sx={{ mx: 1 }}
                        >
                            Facebook
                        </Button>
                        <Button
                            variant="outlined"
                            color="primary"
                            startIcon={<GitHubIcon />}
                            fullWidth
                            sx={{ ml: 1 }}
                        >
                            GitHub
                        </Button>
                    </Box>
                </Box>
            )}
        </Box>
    );
};

export default LoginForm;
