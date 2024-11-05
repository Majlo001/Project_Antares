import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, IconButton, Avatar, Menu, MenuItem, Box } from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useNavigate } from 'react-router-dom';

function TopBar({ isLoggedIn, userName, onLogout, onSettings }) {
  const [anchorEl, setAnchorEl] = useState(null);
  
  const navigate = useNavigate();

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
        <Typography variant="h6" component="div" 
            sx={{ flexGrow: 1, cursor: 'pointer' }}
            onClick={() => { navigate("/"); }}
        >
          Project Antares
        </Typography>

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
                    vertical: 'bottom',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
            >
                <MenuItem onClick={() => { handleMenuClose(); onSettings(); }}>Ustawienia konta</MenuItem>
                <MenuItem onClick={() => { handleMenuClose(); onLogout(); }}>Wyloguj</MenuItem>
            </Menu>
          </Box>
        ) : (
          <IconButton color="inherit"
            sx={{ cursor: 'pointer' }}
            onClick={() => { navigate("/login"); }}
          >
            <AccountCircleIcon />
          </IconButton>
        )}
      </Toolbar>
    </AppBar>
  );
}

export default TopBar;
