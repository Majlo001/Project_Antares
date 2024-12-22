import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import basicSsl from '@vitejs/plugin-basic-ssl'

// https://vitejs.dev/config/
export default defineConfig({
	server: {
        host: '0.0.0.0',
        https: true,
		proxy: {
			'/api': {
				target: 'https://192.168.0.100:8443',
                changeOrigin: true,
                secure: false,
			}
		}
	},
    
    plugins: [
        react(),
        basicSsl()
    ],
})
