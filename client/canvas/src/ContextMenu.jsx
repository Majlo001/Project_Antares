import React from 'react';

const ContextMenu = ({ x, y, options, onSelect }) => {
  return (
    <div style={{
      position: 'absolute',
      top: y,
      left: x,
      backgroundColor: 'white',
      border: '1px solid black',
      zIndex: 1000,
    }}>
      <ul style={{ listStyleType: 'none', margin: 0, padding: 0 }}>
        {options.map((option, index) => (
          <li key={index} style={{ padding: '8px', cursor: 'pointer' }} onClick={() => onSelect(option)}>
            {option.label}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ContextMenu;