import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import LocationDetail from './LocationDetail';
import { BrowserRouter as Router } from 'react-router-dom'; // Dodaj Router, aby obsługiwać useParams
import { request } from './helpers/axios_helper';
import '@testing-library/jest-dom';  // Importuj jest-dom, aby używać toBeInTheDocument

// Mocks dla requesta (symulacja pobierania danych)
jest.mock('./helpers/axios_helper', () => ({
  request: jest.fn().mockResolvedValue({
    data: {
      name: 'Test Location',
      cityName: 'Test City',
      address: 'Test Address',
      description: 'Test Description',
      googleMapsLink: 'https://maps.google.com',
      mainImage: '/path/to/image.jpg',
      events: [],
      websiteUrl: 'https://example.com', // Mockujemy obecność websiteUrl
    },
  }),
}));

describe('LocationDetail Component', () => {
  // Test 1: Sprawdzenie, czy komponent renderuje się poprawnie
  test('renders LocationDetail component', async () => {
    render(
      <Router>
        <LocationDetail />
      </Router>
    );

    // Czekamy na załadowanie danych
    await waitFor(() => screen.getByText('Test Location'));

    // Sprawdzamy, czy komponent renderuje nazwę lokalizacji
    expect(screen.getByText('Test Location')).toBeInTheDocument();
  });

  // Test 2: Sprawdzenie, czy przycisk "Show on map" jest obecny
  test('shows "Show on map" button', async () => {
    render(
      <Router>
        <LocationDetail />
      </Router>
    );

    // Czekamy na załadowanie danych
    await waitFor(() => screen.getByText('Test Location'));

    // Znajdujemy przycisk "Show on map"
    const mapButton = screen.getByText('Show on map');
    
    // Sprawdzamy, czy przycisk jest w dokumencie
    expect(mapButton).toBeInTheDocument();
  });

  // Test 3: Sprawdzenie, czy przycisk "Website" (z ikoną) jest obecny
  test('shows "Website" button when websiteUrl exists', async () => {
    render(
      <Router>
        <LocationDetail />
      </Router>
    );

    // Czekamy na załadowanie danych
    await waitFor(() => screen.getByText('Test Location'));

    // Sprawdzamy, czy przycisk "Website" z ikoną jest obecny
    const websiteButton = screen.getByRole('button', { name: /language/i });

    // Sprawdzamy, czy przycisk jest w dokumencie
    expect(websiteButton).toBeInTheDocument();
  });
});
