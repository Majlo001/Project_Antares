import React, { useState, useEffect, useContext } from "react";
import { Stage, Layer, Rect, Text, Group } from "react-konva";
import { Box, Typography, Badge } from '@mui/material';
import AccessibleRoundedIcon from '@mui/icons-material/AccessibleRounded';
import { request } from "./helpers/axios_helper";
import { useParams } from "react-router-dom";
import { set } from "date-fns";
import { CartContext } from './contexts/CartContext';

const LocationSeatChart = () => {
    const { updateCart } = useContext(CartContext);

    const { eventId } = useParams();
    const [scale, setScale] = useState(1);
    const [position, setPosition] = useState({ x: 0, y: 500 });
    const [seatTooltip, setSeatTooltip] = useState({ visible: false, x: 0, y: 0, text: '' });

    const [locationVariantId, setLocationVariantId] = useState(null);
    const [locationName, setLocationName] = useState('');
    const [maxReservationsPerUser, setMaxReservationsPerUser] = useState(0);
    const [forceChoosingWithoutBreaks, setForceChoosingWithoutBreaks] = useState(false);
    const [sectors, setSectors] = useState([]);

    const [isSeatChartLoaded, setIsSeatChartLoaded] = useState(false);
    const [selectedSeats, setSelectedSeats] = useState([]);

    useEffect(() => {
        console.log("Selected seats:", selectedSeats);
    }, [selectedSeats, eventId]);



    useEffect(() => {
        request("GET", `/api/location_seat_chart/get_chart?eventId=${eventId}`).then((response) => {
            setLocationVariantId(response.data.locationVariantId);
            setLocationName(response.data.locationName);
            setMaxReservationsPerUser(response.data.maxReservationsPerUser);
            setForceChoosingWithoutBreaks(response.data.forceChoosingWithoutBreaks);

            setSectors(response.data.sectors);
            console.log("Pobrano szczegóły wydarzenia:", response.data);
            setIsSeatChartLoaded(true);

        }).catch((error) => {
            console.error("Błąd przy pobieraniu szczegółów wydarzenia:", error.response);
        });
    }, [eventId]);



    useEffect(() => {
        if (!isSeatChartLoaded) return;

        request("GET", `/api/location_seat_chart/get_available_seats?eventId=${eventId}`).then((response) => {
            // console.log("Pobrano dostępne:", response.data);

            const newSectors = [...sectors];
            newSectors.forEach(sector => {
                sector.rows.forEach(row => {
                    row.seats.forEach(seat => {
                        const availableSeat = response.data.find(available => available.seatId === seat.id);
                        seat.seatAvailable = availableSeat ? availableSeat.available : false;
                    });
                });
            });

            setSectors(newSectors);
            console.log("New sectors: ", sectors);

        }).catch((error) => {
            console.error("Błąd przy pobieraniu dostępnych:", error.response);
        });

        if (sessionStorage.getItem('selectedSeatsMap')) {
            const selectedSeatsMap = JSON.parse(sessionStorage.getItem('selectedSeatsMap')) || {};
            if (selectedSeatsMap[eventId]) {
                setSelectedSeats(selectedSeatsMap[eventId]);
            }

            updateCart(eventId, selectedSeatsMap[eventId]);
        }
        console.log("Selected seats:", selectedSeats);
        setIsSeatChartLoaded(false);
    }, [isSeatChartLoaded]);



    const loadSectorSeatStatus = (sectorId) => {
        request("GET", `/api/location_seat_chart/get_available_sector_seats?eventId=${eventId}&sectorId=${sectorId}`).then((response) => {
                                    
            const newSectors = [...sectors];
            const updatedSector = newSectors.find(s => s.id === sectorId);
            if (updatedSector) {
                updatedSector.rows.forEach(row => {
                    row.seats.forEach(seat => {
                        const availableSeat = response.data.find(available => available.seatId === seat.id);
                        seat.seatAvailable = availableSeat ? availableSeat.available : false;
                    });
                });
            }
            
            setSectors(newSectors);
            console.log("New sectors: ", sectors);
        }).catch((error) => {
            console.error("Błąd przy pobieraniu dostępnych miejsc na sektorze:", error.response);
        });
    }



    useEffect(() => {
        const interval = setInterval(() => {
            const selectedSeatsMap = JSON.parse(sessionStorage.getItem('selectedSeatsMap')) || {};
            const eventSeats = selectedSeatsMap[eventId] || [];
            
            console.log("Raw eventSeats:", eventSeats);
            const activeSeats = [];
            const expiredSeats = [];

            eventSeats.forEach(seatArray => {
                if (seatArray[0]?.expirationDate) {
                    const expirationTime = new Date(seatArray[0].expirationDate).getTime();

                    if (isNaN(expirationTime)) {
                        console.error("Invalid expirationDate format for seat:", seatArray);
                    } else if (expirationTime > Date.now()) {
                        activeSeats.push(seatArray);
                    } else {
                        expiredSeats.push(seatArray);
                    }
                } else {
                    console.error("Missing expirationDate for seat:", seatArray);
                }
            });



            console.log("Event seats:", eventSeats);
            console.log("Active seats:", activeSeats);
            console.log("Expired seats:", expiredSeats);
    
            if (expiredSeats.length > 0) {
                const updatedSelectedSeats = selectedSeats.filter(
                    (selSeat) => !expiredSeats.some(expSeat => expSeat[0]?.seatId === selSeat[0]?.seatId)
                );
                setSelectedSeats(updatedSelectedSeats);
    
                const selectedSeatsMap = JSON.parse(sessionStorage.getItem('selectedSeatsMap')) || {};
                selectedSeatsMap[eventId] = updatedSelectedSeats;
                sessionStorage.setItem('selectedSeatsMap', JSON.stringify(selectedSeatsMap));
    

                updateCart(eventId, updatedSelectedSeats);

                const affectedSectors = [...new Set(expiredSeats.map(seatArray => seatArray[0]?.sectorId))];
                affectedSectors.forEach(sectorId => {
                    loadSectorSeatStatus(sectorId);
                });
            }
        }, 30000);  // 30 second interval
    
        return () => clearInterval(interval);
    }, [eventId, selectedSeats]);



    return (
        <div style={{ position: 'relative' }}>
            {seatTooltip.visible && (
                <Box
                    sx={{
                        position: 'absolute',
                        left: seatTooltip.x,
                        top: seatTooltip.y,
                        bgcolor: 'white',
                        padding: '8px 12px',
                        borderRadius: '4px',
                        width: '200px',
                        boxShadow: 2,
                        pointerEvents: 'none',
                        zIndex: 1000,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'flex-start',
                        justifyContent: 'center',
                        fontSize: '14px',
                        color: 'black',
                    }}
                >
                    <Typography>{seatTooltip.sectorName}</Typography>
                    <Typography>{"Seat number " + seatTooltip.seatNumber}</Typography>
                    <Typography>{"Row number " + seatTooltip.rowNumber}</Typography>
                    {seatTooltip.ticketPrices && seatTooltip.ticketPrices.map((ticket, ticketIndex) => (
                        <Typography key={ticketIndex}>{`${ticket.ticketType}: ${ticket.price} PLN`}</Typography>
                    ))}
                </Box>
            )}
            <Stage
                width={1200}
                height={800}
                scaleX={scale}
                scaleY={scale}
                x={position.x}
                y={position.y}
                draggable
                onWheel={(e) => {
                    e.evt.preventDefault();
                    const newScale = e.evt.deltaY > 0 ? scale - 0.1 : scale + 0.1;
                    setScale(Math.max(0.5, Math.min(newScale, 3)));
                }}
                onDragEnd={(e) => {
                    setPosition({
                        x: e.target.x(),
                        y: e.target.y(),
                    });
                }}
            >
                <Layer key={JSON.stringify(selectedSeats)}>
                    {sectors.length > 0 && sectors.map((sector, sectorIndex) => (
                        // Grupa sektora
                        <React.Fragment key={sectorIndex}>
                            {!sector.standing && sector.rows.map((row, rowIndex) => (
                                <Group key={rowIndex}>
                                    {row.seats.map((seat, seatIndex) => {
                                        const seatX = sector.positionX + row.positionX + seat.positionX;
                                        const seatY = sector.positionY + row.positionY + seat.positionY;
                                        const seatRotation = (row.rotation || 0) + (seat.rotation || 0);
                                        const isSelected = selectedSeats.some((selSeat) => selSeat[0].seatId === seat.id);


                                        const handleSeatClick = (seat) => {
                                            if (isSelected) {
                                                const selectedSeatsBody = selectedSeats.find((selSeat) => selSeat[0].seatId === seat.id)[0];
                                                console.log("Unreserving:", selectedSeatsBody);

                                                const body = [
                                                    selectedSeatsBody.seatStatusId,
                                                ];

                                                request("POST", "/api/reservation/unreserve", body, null, {
                                                    headers: {
                                                        'Content-Type': 'application/json'
                                                    }
                                                }).then(() => {
                                                    setSelectedSeats(selectedSeats.filter((selSeat) => selSeat[0].seatId !== seat.id));

                                                    const selectedSeatsMap = JSON.parse(sessionStorage.getItem('selectedSeatsMap')) || {};
                                                    selectedSeatsMap[eventId] = selectedSeats.filter((selSeat) => selSeat[0].seatId !== seat.id);
                                                    sessionStorage.setItem('selectedSeatsMap', JSON.stringify(selectedSeatsMap));
                                                

                                                    updateCart(eventId, selectedSeats.filter((selSeat) => selSeat[0].seatId !== seat.id));
                                                    loadSectorSeatStatus(sector.id);
                                                }).catch((error) => {
                                                    console.error("Błąd przy odrezerwacji:", error.response);
                                                    loadSectorSeatStatus(sector.id);
                                                });

                                            }
                                            else
                                                if (seat.seatAvailable && maxReservationsPerUser > selectedSeats.length) {
                                                    const body = [
                                                        {
                                                            seatId: seat.id,
                                                            eventId: parseInt(eventId),
                                                            sectorId: sector.id,
                                                        }
                                                    ];
                                                    console.log("Zarezerwowano:", body);
                                                    request("POST", "/api/reservation/reserve", body).then((response) => {
                                                        console.log("reservation sector:", sector);
                                                        body.forEach((seatData, seatId) => {
                                                            seatData.seatNumber = seat.seatNumber;
                                                            seatData.rowNumber = row.rowNumber;
                                                            seatData.seatStatusId = response.data[seatId];
                                                            seatData.ticketPriceId = sector.ticketPrices[0].id
                                                            // seatData.ticketTypeId = sector.ticketPrices[0].id
                                                            seatData.expirationDate = new Date(new Date().getTime() + 15 * 60000);
                                                        });
                                                        const updatedSelectedSeats = [...selectedSeats, body];
                                                        setSelectedSeats(updatedSelectedSeats);
                                                        console.log("Selected seats:", updatedSelectedSeats);
                                                        
                                                        // sessionStorage.setItem('selectedSeats', JSON.stringify(updatedSelectedSeats));
                                                        const selectedSeatsMap = JSON.parse(sessionStorage.getItem('selectedSeatsMap')) || {};
                                                        selectedSeatsMap[eventId] = updatedSelectedSeats;
                                                        sessionStorage.setItem('selectedSeatsMap', JSON.stringify(selectedSeatsMap));


                                                        updateCart(eventId, updatedSelectedSeats);
                                                        loadSectorSeatStatus(sector.id);


                                                    }).catch((error) => {
                                                        console.error("Błąd przy rezerwacji:", error.response);
                                                        if (error.response.status === 400) {
                                                            console.log(error.response.data);
                                                        }
                                                        if (error.response.status === 500) {
                                                            console.log(error.response.data);
                                                            loadSectorSeatStatus(sector.id);
                                                        }
                                                    });
                                                }



                                        };

                                        return (
                                            <Group
                                                key={seatIndex}
                                                onMouseOver={() => {
                                                    const tooltipX = seatX * scale + position.x - 200 - Math.ceil(10 * scale);
                                                    const tooltipY = seatY * scale + position.y;

                                                    if (seat.seatAvailable) {
                                                        setSeatTooltip({
                                                            visible: true,
                                                            x: tooltipX,
                                                            y: tooltipY,
                                                            sectorName: (sector.name || "Sector" + sector.id),
                                                            seatNumber: seat.seatNumber,
                                                            rowNumber: row.rowNumber,
                                                            ticketPrices: sector.ticketPrices,
                                                        });
                                                    }
                                                }}
                                                onMouseOut={() => {
                                                    setSeatTooltip({ visible: false, x: -100, y: -100, text: "" });
                                                }}
                                                onClick={() => handleSeatClick(seat)}
                                            >
                                                
                                                <Rect
                                                    x={seatX}
                                                    y={seatY}
                                                    rotation={seatRotation}
                                                    width={10}
                                                    height={10}
                                                    fill={isSelected ? "blue" : !seat.seatAvailable ? "gray" : "red"}
                                                    stroke="black"
                                                    strokeWidth={1}
                                                />
                                                {/* {seat.seatForDisabled && (
                                                    <div
                                                        style={{
                                                            position: 'absolute',
                                                            left: seatX * scale + position.x,
                                                            top: seatY * scale + position.y,
                                                            transform: `rotate(${seatRotation}deg)`,
                                                            fontSize: 10,
                                                            color: 'yellow',
                                                            pointerEvents: 'none',
                                                            zIndex: 1000,
                                                        }}
                                                    >
                                                        <AccessibleRoundedIcon />
                                                    </div>
                                                )} */}
                                                <Text
                                                    text={isSelected ? "" : !seat.seatAvailable ? "" : seat.seatNumber}
                                                    pointerEvents="none"
                                                    width={10}
                                                    height={10}
                                                    fontSize={5}
                                                    fill="white"
                                                    weight="bold"
                                                    x={seatX}
                                                    y={seatY}
                                                    align="center"
                                                    verticalAlign="middle"
                                                    rotation={seatRotation}
                                                />
                                            </Group>
                                        );
                                    })}
                                </Group>
                            ))}
                        </React.Fragment>
                    ))}
                </Layer>
            </Stage>
        </div>
    );
};

export default LocationSeatChart;
