import React from 'react';
import { useDrag } from 'react-dnd';

const ItemTypes = {
  OBJECT: 'object',
};

const DraggableObject = ({ type }) => {
  const [{ isDragging }, drag] = useDrag(() => ({
    type: ItemTypes.OBJECT,
    item: { type },
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging(),
    }),
  }));

  return (
    <div
      ref={drag}
      style={{
        opacity: isDragging ? 0.5 : 1,
        cursor: 'move',
        padding: '8px',
        margin: '4px',
        backgroundColor: 'lightgray',
        border: '1px solid black',
      }}
    >
      {type}
    </div>
  );
};

const Sidebar = () => {
  return (
    <div style={{ width: '200px', padding: '10px', borderRight: '1px solid black' }}>
      <h3>Panel obiektów</h3>
      <DraggableObject type="Rectangle" />
      <DraggableObject type="Circle" />
    </div>
  );
};

export default Sidebar;
