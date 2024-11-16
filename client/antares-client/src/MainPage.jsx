import React, { useState, useEffect } from 'react';
import { CssBaseline, Container } from '@mui/material';
import EventList from './EventList';
import NewsletterSignup from './NewsletterSignup';


const MainPage = () => {

    
    return (
        <Container>
            <CssBaseline />
            <EventList />
            <NewsletterSignup />
        </Container>
    );
};

export default MainPage;