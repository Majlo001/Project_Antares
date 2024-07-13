import React, { useState, useRef, useEffect } from 'react';
import { Stage, Layer, Rect, Line } from 'react-konva';
import convexHull from 'convex-hull';
import { useDrop } from 'react-dnd';

const ItemTypes = {
	OBJECT: 'object',
};

const DraggableCanvas = () => {
  const [shapes, setShapes] = useState([
    { id: 'rect1', x: 50, y: 200, width: 50, height: 50, isDragging: false, selected: false},
    { id: 'rect2', x: 101, y: 200, width: 50, height: 50, isDragging: false, selected: false},
    { id: 'rect3', x: 152, y: 200, width: 50, height: 50, isDragging: false, selected: false},
  ]);
  const [selectionBox, setSelectionBox] = useState(null);
  const [hullPoints, setHullPoints] = useState([]);
  const stageRef = useRef(null);
  const layerRef = useRef(null);
  const startPositionsRef = useRef({});

  
  const [scale, setScale] = useState(1);
  const [stagePosition, setStagePosition] = useState({ x: 0, y: 0 });
  const minScale = 0.1;
  const maxScale = 3;


	const [, drop] = useDrop(() => ({
    accept: ItemTypes.OBJECT,
    drop: (item, monitor) => {
      const offset = monitor.getClientOffset();
      if (!offset) return;

      setShapes((prevShapes) => [
        ...prevShapes,
        {
          id: `rect${prevShapes.length + 1}`,
          type: item.type,
          x: offset.x - 150,
          y: offset.y - 25,
          width: 50,
          height: 50,
          isDragging: false,
          selected: false,
        },
      ]);
    },
  }));

  useEffect(() => {
    const handleKeyDown = (e) => {
      if (e.ctrlKey && e.key === 'd') {
        e.preventDefault();
        duplicateSelectedShapes();
      }
      if (e.ctrlKey && e.key === 'h') {
        e.preventDefault();
        drawConvexHull();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [shapes]);

  useEffect(() => {
    const stage = stageRef.current;

    const handleWheel = (e) => {
      e.evt.preventDefault();
      const scaleBy = 1.1;
      const oldScale = stage.scaleX();
      const mousePointTo = {
        x: stage.getPointerPosition().x / oldScale - stage.x() / oldScale,
        y: stage.getPointerPosition().y / oldScale - stage.y() / oldScale,
      };

      if (oldScale < minScale && e.evt.deltaY > 0) return;
      if (oldScale > maxScale && e.evt.deltaY < 0) return;

      const newScale = e.evt.deltaY > 0 ? oldScale / scaleBy : oldScale * scaleBy;
      setScale(newScale);
      console.log(newScale);
      setStagePosition({
        x: -(mousePointTo.x - stage.getPointerPosition().x / newScale) * newScale,
        y: -(mousePointTo.y - stage.getPointerPosition().y / newScale) * newScale,
      });
    };

    stage.on('wheel', handleWheel);

    return () => {
      stage.off('wheel', handleWheel);
    };
  }, []);




  const duplicateSelectedShapes = () => {
    let counterShapes = 1;
    const selectedShapes = shapes.filter((shape) => shape.selected);
    selectedShapes.forEach((shape) => {
        shape.selected = false;
    });
    const newShapes = selectedShapes.map((shape) => {
      const newId = `rect${shapes.length + counterShapes}`;
      counterShapes++;
      return {
        ...shape,
        id: newId,
        x: shape.x + 10,
        y: shape.y + 10,
        isDragging: false,
        selected: true,
      };
    });

    setShapes([...shapes, ...newShapes]);
  };

  const drawConvexHull = () => {
    const selectedShapes = shapes.filter((shape) => shape.selected);
    if (selectedShapes.length < 3) return;

    const points = [];
    selectedShapes.forEach(shape => {
      points.push([shape.x, shape.y]);
      points.push([shape.x + shape.width, shape.y]);
      points.push([shape.x, shape.y + shape.height]);
      points.push([shape.x + shape.width, shape.y + shape.height]);
    });

    const hull = convexHull(points).map(([i]) => points[i]);

    // Przekształć punkty na format, który Konva może użyć
    const flattenedHull = hull.reduce((acc, point) => acc.concat(point), []);
    setHullPoints(flattenedHull);
  };

  

  const handleDragStart = (e) => {
    const id = e.target.id();
    const newStartPositions = {};
    shapes.forEach((shape) => {
      if (shape.id === id || shape.selected) {
        newStartPositions[shape.id] = { x: shape.x, y: shape.y };
      }
    });
    startPositionsRef.current = newStartPositions;
    setShapes(
      shapes.map((shape) => ({
        ...shape,
        isDragging: shape.id === id || shape.selected,
      }))
    );
  };

  
  const handleDragMove = (e) => {
      const id = e.target.id();
      const { x, y } = e.target.position();
      const { current: startPositions } = startPositionsRef;
      const dx = x - startPositions[id].x;
      const dy = y - startPositions[id].y;

      layerRef.current.children.forEach((child) => {
        const childId = child.id();
        const isSelected = shapes.find((shape) => shape.id === childId).selected;

        if (isSelected && child !== e.target) {
          child.x(child.x() + dx);
          child.y(child.y() + dy);
        }
      });

      const newStartPositions = {};
      shapes.forEach((shape) => {
        if (shape.id === id) {
          newStartPositions[shape.id] = { x: x, y: y };
        }
      });
      startPositionsRef.current = newStartPositions;
    };
  
    const handleDragEnd = (e) => {
      setShapes(
        shapes.map((shape) => {
          if (shape.isDragging) {
            const targetShape = layerRef.current.children.find(child => child.id() === shape.id); 
            if (targetShape) { 
              return { 
                ...shape, 
                x: Math.round(targetShape.x() / 10) * 10, 
                y: Math.round(targetShape.y() / 10) * 10, 
                isDragging: false, 
              }; 
            } 
          }
          return shape;
        }
      ));
        
      startPositionsRef.current = {};
    };
  


  const handleMouseDown = (e) => {
    if (e.target === stageRef.current) {
      const pos = stageRef.current.getPointerPosition();
      setSelectionBox({ x: pos.x, y: pos.y, width: 0, height: 0 });
    }
  };

  const handleMouseMove = (e) => {
    if (!selectionBox) return;
    const pos = stageRef.current.getPointerPosition();
    setSelectionBox({
      ...selectionBox,
      width: pos.x - selectionBox.x,
      height: pos.y - selectionBox.y,
    });
  };

  const handleMouseUp = () => {
    if (!selectionBox) return;

    const newShapes = shapes.map((shape) => {
      const { x, y, width, height } = selectionBox;
      const isSelected =
        shape.x > x &&
        shape.x < x + width &&
        shape.y > y &&
        shape.y < y + height;
      return { ...shape, selected: isSelected };
    });

    setShapes(newShapes);
    setSelectionBox(null);
  };

  return (
		<div ref={drop} style={{ flex: 1, padding: '10px' }}>
			<Stage
				width={2*window.innerWidth}
				height={2* window.innerHeight}
				// draggable
				scaleX={scale}
				scaleY={scale}
				x={stagePosition.x}
				y={stagePosition.y}
				onMouseDown={handleMouseDown}
				onMouseMove={handleMouseMove}
				onMouseUp={handleMouseUp}
				ref={stageRef}
			>
				<Layer 
					ref={layerRef}
				>
					{shapes.map((shape) => (
						<Rect
							key={shape.id}
							id={shape.id}
							x={shape.x}
							y={shape.y}
							width={shape.width}
							height={shape.height}
							fill={shape.selected ? 'green' : 'red'}
							draggable
							onDragStart={handleDragStart}
							onDragMove={handleDragMove}
							onDragEnd={handleDragEnd}
						/>
					))}
					{selectionBox && (
						<Rect
							x={selectionBox.x}
							y={selectionBox.y}
							width={selectionBox.width}
							height={selectionBox.height}
							fill="rgba(0, 0, 255, 0.2)"
							stroke="blue"
							strokeWidth={1}
						/>
					)}
					{hullPoints.length > 0 && (
						<Line
							points={hullPoints.concat(hullPoints.slice(0, 2))}
							stroke="purple"
							strokeWidth={2}
							closed
						/>
					)}
				</Layer>
			</Stage>
		</div>
  );
};

export default DraggableCanvas;
