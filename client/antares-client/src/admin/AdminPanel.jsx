import React from 'react';
import { Box, Drawer, List, ListItem, ListItemText, Typography } from '@mui/material';
import AdminPanelSidebar from './AdminPanelSidebar';
import CreateEventForm from '../creationForms/CreateEventForm';


const AdminPanel = () => {

    return (
        <Box sx={{ display: 'flex' }}>
            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <Typography variant="h4">Welcome to the Admin Panel</Typography>
            </Box>
        </Box>
    );
};

export default AdminPanel;