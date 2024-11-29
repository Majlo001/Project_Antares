import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Box, Drawer, List, ListItem, ListItemText, Typography } from '@mui/material';

const AdminPanelSidebar = ( isLoggedIn, userName ) => {
    const location = useLocation();
    const navigate = useNavigate();
    const drawerWidth = 240;


    // useEffect(() => {
    //     console.log("AdminPanelSidebar: ", isLoggedIn, userName);
    // }, [isLoggedIn, userName]);

    return (
        <>
        {location.pathname.startsWith('/admin') && (
            <Drawer
                sx={{
                    // width: drawerWidth,
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        // width: drawerWidth,
                        boxSizing: 'border-box',
                        paddingTop: 6,
                        paddingBottom: 6,
                    },
                }}
                variant="permanent"
                anchor="left"
            >
                <Box sx={{ p: 2 }}>
                    <Typography variant="h6">Antares Admin Panel</Typography>
                </Box>
                <List sx={{ flexGrow: 1 }}>
                    <ListItem button>
                        <ListItemText
                            primary="Menage Events"
                            onClick={() => navigate('/admin/events')}
                            sx={{ cursor: 'pointer'}} />
                    </ListItem>
                    <ListItem button>
                        <ListItemText
                            primary="Create Event"
                            onClick={() => navigate('/admin/form/event')}
                            sx={{ cursor: 'pointer'}} />
                    </ListItem>
                    <ListItem button>
                        <ListItemText primary="Menage Event Series" />
                    </ListItem>
                    <ListItem button>
                        <ListItemText primary="Create Event Series" />
                    </ListItem>
                </List>
                <Box sx={{ p: 2 }}>
                    {/* <Typography>{userName}</Typography> */}
                </Box>
            </Drawer>
        )}
        </>
    );
};

export default AdminPanelSidebar;