import React from "react";
import { Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend, Title } from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';

ChartJS.register(ArcElement, Tooltip, Title, Legend, ChartDataLabels);

const SeatsChart = () => {
    const data = {
        labels: ["Available seats", "Reserved seats"],
        datasets: [
            {
                label: "Seats",
                data: [120, 80],
                backgroundColor: ["#36A2EB", "#FF6384"],
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            title: { display: true, text: "Available and reserved seats" },
            legend: {
                display: true,
                position: "bottom",
            },
            tooltip: {
                callbacks: {
                    label: (context) => {
                        const value = context.raw;
                        const total = context.dataset.data.reduce((acc, val) => acc + val, 0);
                        const percentage = ((value / total) * 100).toFixed(1);
                        return `${context.label}: ${value}\n(${percentage}%)`;
                    },
                },
            },
            datalabels: {
                display: true,
                color: "white",
                font: {
                    weight: "bold",
                    size: 14,
                },
                formatter: (value, context) => {
                    const total = context.dataset.data.reduce((acc, val) => acc + val, 0);
                    const percentage = ((value / total) * 100).toFixed(1);
                    return `${value} (${percentage}%)`;
                },
            },
        },
    };

  return <Doughnut data={data} options={options} />;
};

export default SeatsChart;
