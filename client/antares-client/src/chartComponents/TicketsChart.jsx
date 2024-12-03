import React from "react";
import { Line } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const TicketsChart = () => {
    const data = {
        labels: ["Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela"],
        datasets: [
            {
                label: "Sprzedane bilety",
                data: [10, 15, 8, 20, 30, 25, 18],
                borderColor: "#FF6384",
                backgroundColor: "rgba(255,99,132,0.2)",
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            legend: { display: true },
            title: { display: true, text: "Count of sold tickets this week" },
        },
    };

    return <Line data={data} options={options} />;
};

export default TicketsChart;
