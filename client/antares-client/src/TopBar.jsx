import React, { useEffect, useState, useContext } from 'react';
import { AppBar, Toolbar, Typography, IconButton, Avatar, Menu, MenuItem, Box, Badge } from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { useNavigate } from 'react-router-dom';
import { CartContext } from "./contexts/CartContext";

function TopBar({ isLoggedIn, userName, onLogout, onSettings }) {
    const { cartData, calculateCartItems } = useContext(CartContext);
    const [cartItemCount, setCartItemCount] = useState(0);

    const [anchorEl, setAnchorEl] = useState(null);
    const navigate = useNavigate();
    

    useEffect(() => {
        const count = calculateCartItems();
        console.log("TopBar: count:", count);
        setCartItemCount(count);
    }, [cartData, calculateCartItems]);

    const handleMenuOpen = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    useEffect(() => {
        console.log("TopBar: isLoggedIn:", isLoggedIn);
    }, [isLoggedIn]);

  return (
    <AppBar position="static" sx={{ mb: 4 }}>
      <Toolbar>
        <Typography variant="h6" component="div" 
            sx={{ flexGrow: 1, cursor: 'pointer' }}
            onClick={() => { navigate("/"); }}
        >
          Antares Ticket App
        </Typography>

        <IconButton 
          color="inherit"
          onClick={() => navigate("/cart")}
          sx={{ mr: 2 }}
        >
            <Badge badgeContent={cartItemCount} color="secondary">
                <ShoppingCartIcon />
            </Badge>
        </IconButton>

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
