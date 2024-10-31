import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, IconButton, Avatar, Menu, MenuItem, Box } from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

function TopBar({ isLoggedIn, userName, onLogout, onSettings }) {
  const [anchorEl, setAnchorEl] = useState(null);

  // Obsługa otwierania i zamykania menu
  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  return (
    <AppBar position="static" sx={{ mb: 4 }}>
      <Toolbar>
        {/* Logo aplikacji */}
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Project Antares
        </Typography>

        {/* Ikonka użytkownika */}
        {isLoggedIn ? (
          <Box>
            <IconButton onClick={handleMenuOpen} color="inherit">
              <Avatar>{userName[0]}</Avatar>
            </IconButton>
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                MenuListProps={{
                    onMouseLeave: handleMenuClose,
                }}
                anchorOrigin={{
                    vertical: 'bottom', // Menu pojawi się pod ikoną
                    horizontal: 'right', // Menu będzie wyrównane do prawej
                }}
                transformOrigin={{
                    vertical: 'top', // Ustawia transformację od góry menu
                    horizontal: 'right', // Ustawia transformację od prawej
                }}
            >
                <MenuItem onClick={() => { handleMenuClose(); onSettings(); }}>Ustawienia konta</MenuItem>
                <MenuItem onClick={() => { handleMenuClose(); onLogout(); }}>Wyloguj</MenuItem>
            </Menu>
          </Box>
        ) : (
          <IconButton color="inherit">
            <AccountCircleIcon />
          </IconButton>
        )}
      </Toolbar>
    </AppBar>
  );
}

export default TopBar;
