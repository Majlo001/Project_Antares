import React, { useState, useEffect } from 'react';
import { request } from './helpers/axios_helper';

const EventList = () => {
    const [events, setEvents] = useState([]);
    const [pageNo, setPageNo] = useState(1);
    const [pageSize] = useState(2);
    const [totalPages, setTotalPages] = useState(1);

    // Funkcja do pobierania eventów z serwera
    const fetchEvents = (page = 1) => {
        request("GET", `/api/events?page=${page}&size=${pageSize}`)
            .then((response) => {
                console.log("Pobrano eventy:", response.data.events);
                setEvents(response.data.events);
                setPageNo(response.data.pageNo + 1);
                setTotalPages(response.data.totalPages);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania eventów:", error);
            });
    };

    // Pobieranie eventów przy pierwszym renderze
    useEffect(() => {
        fetchEvents();
    }, []);

    // Obsługa zmiany strony
    const handlePageChange = (newPage) => {
        fetchEvents(newPage);
    };

    return (
        <div>
            <h2>Lista dostępnych wydarzeń</h2>
            <ul>
                {events.map((event, index) => (
                    <li key={index}>
                        <h3>{event.name}</h3>
                        <p>{event.description}</p>
                        <p>Data: {new Date(event.dateStart).toLocaleString()}</p>
                    </li>
                ))}
            </ul>

            <div>
                {pageNo > 1 && (
                    <button onClick={() => handlePageChange(pageNo - 1)}>Poprzednia strona</button>
                )}
                {pageNo < totalPages && (
                    <button onClick={() => handlePageChange(pageNo + 1)}>Następna strona</button>
                )}
            </div>
        </div>
    );
};

export default EventList;
