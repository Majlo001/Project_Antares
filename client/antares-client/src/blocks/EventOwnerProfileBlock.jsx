import React from 'react';
import { Box, Typography, Avatar } from '@mui/material';
import ArrowForwardRoundedIcon from '@mui/icons-material/ArrowForwardRounded';
import { serverBaseUrl } from '../helpers/settings';
import { truncateText } from '../helpers/text_helper';

function EventOwnerProfileBlock({ imageUrl, name, id }) {
    return (
        <Box
            component="a"
            href={`/organisator/${id}`}
            sx={{
                display: 'flex',
                alignItems: 'center',
                p: 2,
                width: 'fit-content',
                textDecoration: 'none',
                color: 'inherit',
                position: 'relative',
                overflow: 'hidden',
                boxShadow: 'none',

                '&:hover .event-owner-name': {
                    color: 'primary.main',
                },
                
            }}
        >
            <Avatar
                src={serverBaseUrl + imageUrl}
                alt={name}
                sx={{
                    width: 50,
                    height: 50,
                    mr: 2,
                    borderRadius: '50%',
                }}
            />
            
            <Box sx={{ flex: 1 }}>
                <Typography
                    variant="h6"
                    component="div"
                    className='event-owner-name'
                    gutterBottom
                    sx={{
                        color: 'text.secondary',
                        transition: 'color 0.3s ease',
                    }}>
                    {name}
                </Typography>
            </Box>
        </Box>
    );
}

export default EventOwnerProfileBlock;
