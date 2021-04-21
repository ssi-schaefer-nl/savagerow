import React, { Component, useState } from 'react';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import './react-contextmenu.css'

const ContextMenu = (props) => {
    const [mouseY, setMouseY] = useState(null)
    const [mouseX, setMouseX] = useState(null)

    const menuItems = Object.keys(props.menuItems).map((k) => 
        <MenuItem onClick={() => props.onItemClick(props.menuItems[k])}>{props.menuItems[k]}</MenuItem>
    )
    
    const handleClose = () => {
        setMouseX(null)
        setMouseY(null)
    }
    const handleClick = (e) => {
        e.preventDefault()
        setMouseX(e.clientX - 2)
        setMouseY(e.clientY - 4)
    }

    return (
        <div onClick={handleClose} onContextMenu={handleClick} style={{ cursor: 'context-menu' }}>
            {props.children}
            <Menu
                keepMounted
                open={mouseY !== null}
                onClose={handleClose}
                anchorReference="anchorPosition"
                anchorPosition={
                    mouseY !== null && mouseX !== null
                        ? { top: mouseY, left: mouseX }
                        : undefined
                }
            >
                {menuItems}
            </Menu>
        </div>
    )

}


export default ContextMenu;