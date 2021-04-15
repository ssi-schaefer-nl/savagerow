import React, { Component } from 'react';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import './react-contextmenu.css'

class ContentWithContextMenu extends React.Component {
    state = {
        mouseX: null,
        mouseY: null,
    };

    render() {
        return (
            <div onClick={this.handleClose} onContextMenu={this.handleClick} style={{ cursor: 'context-menu' }}>
                {this.props.content}
                <Menu
                    keepMounted
                    open={this.state.mouseY !== null}
                    onClose={this.handleClose}
                    anchorReference="anchorPosition"
                    anchorPosition={this.state.mouseY !== null && this.state.mouseX !== null
                        ? { top: this.state.mouseY, left: this.state.mouseX }
                        : undefined
                    }
                >
                    {this.props.menuItems.map((text) => (
                        <MenuItem onClick={() => this.props.action(text)}>{text}</MenuItem>
                    ))}
                </Menu>

            </div>
        )
    }


    handleClick = (event) => {
        event.preventDefault();
        this.setState({
            mouseX: event.clientX - 2,
            mouseY: event.clientY - 4,
        });
    };

    handleClose = () => {
        var initialState = {
            mouseX: null,
            mouseY: null,
        };

        this.setState(initialState);
    };
}

export default ContentWithContextMenu;