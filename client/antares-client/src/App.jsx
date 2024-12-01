import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Box } from '@mui/material';
import { request, setAuthHeader, getDataFromToken } from './helpers/axios_helper';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Container } from '@mui/material';
import LoginForm from './LoginForm';
import AuthContent from './AuthContent';
import MainPage from './MainPage';
import TopBar from './TopBar';
import Cart from './Cart';
import EventDetail from './EventDetail';
import ArtistDetail from './ArtistDetail';
import LocationDetail from './LocationDetail';
import EventsPage from './EventsPage';

import AdminPanelSidebar from './admin/AdminPanelSidebar';
import AdminPanel from './admin/AdminPanel';
import AdminEventsPage from './admin/AdminEventsPage';
import AdminEventDashboard from './admin/AdminEventDashboard';

import CreateEventForm from './creationForms/CreateEventForm';
import LocationSeatChart from './LocationSeatChart';
import PaymentSuccess from './PaymentSuccess';
import PaymentCancel from './PaymentCancel';
import UserTickets from './UserTickets';
import UserTransactions from './UserTransactions';
import { CartProvider } from './contexts/CartContext';

import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

const AppContent = () => {
    const [username, setUsername] = useState(localStorage.getItem("username"));
    const [isLoggedIn, setIsLoggedIn] = useState(localStorage.getItem("auth_token") !== null);

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
        const navigate = useNavigate();
        navigate("/");
        window.location.reload();
    };

    const onLogin = (e, username, password) => {
        e.preventDefault();
        request(
            "POST",
            "/api/auth/login",
            {
                login: username,
                password: password
            }
        ).then((response) => {
            console.log("Zalogowano:", response.data);
            setIsLoggedIn(true);
            setAuthHeader(response.data.token);
            localStorage.setItem("username", username);
            setUsername(username);
        }).catch(() => {
            setAuthHeader(null);
            setUsername(null);
            setIsLoggedIn(false);
        });
    };

    const onRegister = (e, email, firstName, lastName, username, password) => {
        e.preventDefault();
        request(
            "POST",
            "/api/auth/register",
            {
                email: email,
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

    const fetchRole = () => {
        return request("GET", "/api/auth/role", null, null)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                console.error("Error fetching role:", error);
                return null;
            });
    };

    const getRoleEVENT_OWNER = () => {
        return fetchRole().then((role) => {
            return role === "EVENT_OWNER" || role === "ADMIN";
        });
    };

    const getRoleTICKET_CONTROLLER = () => {
        return fetchRole().then((role) => {
            return role === "TICKET_CONTROLLER" || role === "ADMIN";
        });
    };

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
                    />

                    <Routes>
                        <Route path="/" element={<MainPage />} />
                        <Route path="/login" element={<LoginForm onLogin={onLogin} onRegister={onRegister} />} />
                        <Route path="/events" element={<EventsPage />} />
                        <Route path="/events/:eventId" element={<EventDetail />} />
                        <Route path="/auth/*" element={<AuthContent />} />
                        
                        <Route path="/seat_chart/:eventId" element={<LocationSeatChart />} />
                        <Route path="/cart" element={<Cart />} />
                        <Route path="/payment/success" element={<PaymentSuccess />} />
                        <Route path="/payment/cancel" element={<PaymentCancel />} />

                        <Route path="/artists/:artistId" element={<ArtistDetail />} />
                        <Route path="/locations/:locationId" element={<LocationDetail />} />

                        {/* <Route path="/settings" element={} /> */}
                        <Route path="/tickets" element={<UserTickets />} />
                        <Route path="/transaction" element={<UserTransactions />} />
                        
                    </Routes>
                    <Box sx={{ display: 'flex', width: '100%' }}>
                        <AdminPanelSidebar
                            isLoggedIn={isLoggedIn}
                            userName={username}
                        />
                        <Routes>
                            <Route path="/admin/" element={<AdminPanel />} />
                            <Route path="/admin/event/:eventId" element={<AdminEventDashboard />} />
                            <Route path="/admin/events" element={<AdminEventsPage />} />

                            
                            <Route path="/admin/form/event" element={<CreateEventForm />} />
                            <Route path="/admin/form/event/:eventId" element={<CreateEventForm />} />
                        </Routes>
                    </Box>
                </Container>
            </Router>
        </CartProvider>
    );
};

export default AppContent;
