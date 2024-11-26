import React, { useState, useEffect } from 'react';
import EventSearchBar from './blocks/EventSearchBar';
import { Container, Box, CircularProgress } from '@mui/material';
import EventPreviewItem from './blocks/EventPreviewItem';
import { request } from "./helpers/axios_helper";
import { format, startOfDay, endOfDay } from 'date-fns';

const Events = () => {
    const [searchParams, setSearchParams] = useState({
        category: '',
        cityName: '',
        dateStart: '',
        dateEnd: '',
        searchText: ''
    });
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (window.location.search) {
            const params = new URLSearchParams(window.location.search);
            // const dateStart = params.get('dateStart');
            // const dateEnd = params.get('dateEnd');
            
            setSearchParams({
                category: params.get('category') || '',
                cityName: params.get('cityName') || '',
                // dateStart: dateStart ? new Date(dateStart) : '',
                // dateEnd: dateEnd ? new Date(dateEnd) : '',
                searchText: params.get('searchText') || ''
            });
        }
    }, []);

    const handleSearch = () => {
        const { dateStart, dateEnd, category, cityName, searchText } = searchParams;

        // // Sprawdź, czy daty są prawidłowe, zanim je przekształcisz
        // const formattedStartDate = dateStart ? format(startOfDay(dateStart), "yyyy-MM-dd'T'HH:mm:ss") : '';
        // const formattedEndDate = dateEnd ? format(endOfDay(dateEnd), "yyyy-MM-dd'T'HH:mm:ss") : '';

        // setLoading(true);
        // request("GET", `/api/events?category=${category}&cityName=${cityName}&dateStart=${formattedStartDate}&dateEnd=${formattedEndDate}&searchText=${searchText}&page=1&size=10`)
        //     .then((response) => {
        //         setEvents(response.data);
        //         setLoading(false);
        //     })
        //     .catch((error) => {
        //         console.error("Błąd podczas pobierania eventów:", error);
        //         setLoading(false);
        //     });
    };

    return (
        <Container maxWidth="xl" sx={{ mt: 4 }}>
            <EventSearchBar
                isMainPage={false}
                searchParams={searchParams}
                onSearch={handleSearch}
            />
            {/* Loading Spinner */}
            {loading && (
                <Box sx={{ textAlign: 'center', mt: 4 }}>
                    <CircularProgress />
                </Box>
            )}

            {/* Wyświetlanie eventów */}
            <Box sx={{ mt: 4 }}>
                {events.map((event) => (
                    <EventPreviewItem key={event.id} event={event} />
                ))}
            </Box>
        </Container>
    );
};

export default Events;
