import React, { useEffect, useRef } from "react";
import Slider from "react-slick";
import EventSliderItem from './EventSliderItem';



const EventSlider = ({ events }) => {

    const settings = {
        dots: true,
        infinite: true,
        speed: 1000,
        slidesToShow: 5,
        slidesToScroll: 1,
        responsive: [
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 4,
                    slidesToScroll: 1,
                    infinite: true,
                    dots: true
                }
            },
            {
                breakpoint: 600,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 1,
                    initialSlide: 2
                }
            },
            {
                breakpoint: 480,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1
                }
            }
        ]
    };

    return (
        <div className="slider-container">
            <Slider {...settings}>
                {events.map((event, index) => (
                    <EventSliderItem key={index} event={event} index={index} />
                ))}
            </Slider>
        </div>
    );
};

export default EventSlider;