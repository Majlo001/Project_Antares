import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Box, Drawer, List, ListItem, ListItemText, Typography } from '@mui/material';
import { request } from '../helpers/axios_helper';

const AdminPanelSidebar = ({ isLoggedIn, userName }) => {
    const location = useLocation();
    const navigate = useNavigate();
    const drawerWidth = 240;


    return (
        <>
        {location.pathname.startsWith('/admin') && (
            <Drawer
                sx={{
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        boxSizing: 'border-box',
                        width: drawerWidth,
                        paddingTop: 6,
                        paddingBottom: 6,
                        backgroundColor: '#f5f5f5',
                    },
                }}
                variant="permanent"
                anchor="left"
            >
                <Box sx={{ p: 2, textAlign: 'center', backgroundColor: '#FF8D21', color: '#fff' }}>
                    <Typography variant="h6">Event Owner Panel</Typography>
                </Box>
                <List sx={{ flexGrow: 1 }}>
                    <ListItem button onClick={() => navigate('/admin/events')}>
                        <ListItemText primary="Manage Events" sx={{ cursor: 'pointer' }} />
                    </ListItem>
                    <ListItem button onClick={() => navigate('/admin/form/event')}>
                        <ListItemText primary="Create Event" sx={{ cursor: 'pointer' }} />
                    </ListItem>
                    <ListItem button onClick={() => navigate('/admin/event-series')}>
                        <ListItemText primary="Manage Event Series" sx={{ cursor: 'pointer' }} />
                    </ListItem>
                    <ListItem button onClick={() => navigate('/admin/form/event-series')}>
                        <ListItemText primary="Create Event Series" sx={{ cursor: 'pointer' }} />
                    </ListItem>
                </List>
                <Box sx={{ p: 2, textAlign: 'center', borderTop: '1px solid #ddd' }}>
                    <Typography variant="body1">{userName}</Typography>
                </Box>
            </Drawer>
        )}
        </>
    );
};

export default AdminPanelSidebar;