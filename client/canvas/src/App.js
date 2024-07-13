import React from 'react';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import Sidebar from './Sidebar';
import DraggableCanvas from './DraggableCanvas';


// function App() {
//   return (
//     <div className="App">
//       <DraggableCanvas />
//     </div>
//   );
// }

const App = () => {
    return (
      <DndProvider backend={HTML5Backend}>
        <div style={{ display: 'flex' }}>
          <Sidebar />
          <div>
            <DraggableCanvas />
          </div>
        </div>
      </DndProvider>
    );
  };

export default App;
