import React, { useState } from 'react';

const LoginForm = ({ onLogin, onRegister }) => {
    const [active, setActive] = useState("login");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");

    const onChangeHandler = (event) => {
        const { name, value } = event.target;
        switch (name) {
            case "firstName":
                setFirstName(value);
                break;
            case "lastName":
                setLastName(value);
                break;
            case "login":
                setLogin(value);
                break;
            case "password":
                setPassword(value);
                break;
            default:
                break;
        }
    };

    const onSubmitLogin = (e) => {
        e.preventDefault();
        onLogin(e, login, password);
    };

    const onSubmitRegister = (e) => {
        e.preventDefault();
        onRegister(e, firstName, lastName, login, password);
    };

    return (
        <div className="row justify-content-center">
            <div className="col-4">
                <ul className="nav nav-pills nav-justified mb-3" id="ex1" role="tablist">
                    <li className="nav-item" role="presentation">
                        <button 
                            // className={classNames("nav-link", active === "login" ? "active" : "")} 
                            id="tab-login"
                            onClick={() => setActive("login")}
                        >
                            Login
                        </button>
                    </li>
                    <li className="nav-item" role="presentation">
                        <button 
                            // className={classNames("nav-link", active === "register" ? "active" : "")} 
                            id="tab-register"
                            onClick={() => setActive("register")}
                        >
                            Register
                        </button>
                    </li>
                </ul>

                <div className="tab-content">
                    <div id="pills-login">
                        <form onSubmit={onSubmitLogin}>
                            <div className="form-outline mb-4">
                                <input 
                                    type="text" 
                                    id="loginName" 
                                    name="login" 
                                    className="form-control" 
                                    value={login}
                                    onChange={onChangeHandler}
                                />
                                <label className="form-label" htmlFor="loginName">Username</label>
                            </div>

                            <div className="form-outline mb-4">
                                <input 
                                    type="password" 
                                    id="loginPassword" 
                                    name="password" 
                                    className="form-control" 
                                    value={password}
                                    onChange={onChangeHandler}
                                />
                                <label className="form-label" htmlFor="loginPassword">Password</label>
                            </div>

                            <button type="submit" className="btn btn-primary btn-block mb-4">Sign in</button>
                        </form>
                    </div>
                    <div id="pills-register">
                        <form onSubmit={onSubmitRegister}>
                            <div className="form-outline mb-4">
                                <input 
                                    type="text" 
                                    id="firstName" 
                                    name="firstName" 
                                    className="form-control" 
                                    value={firstName}
                                    onChange={onChangeHandler}
                                />
                                <label className="form-label" htmlFor="firstName">First name</label>
                            </div>

                            <div className="form-outline mb-4">
                                <input 
                                    type="text" 
                                    id="lastName" 
                                    name="lastName" 
                                    className="form-control" 
                                    value={lastName}
                                    onChange={onChangeHandler}
                                />
                                <label className="form-label" htmlFor="lastName">Last name</label>
                            </div>

                            <div className="form-outline mb-4">
                                <input 
                                    type="text" 
                                    id="login" 
                                    name="login" 
                                    className="form-control" 
                                    value={login}
                                    onChange={onChangeHandler}
                                />
                                <label className="form-label" htmlFor="login">Username</label>
                            </div>

                            <div className="form-outline mb-4">
                                <input 
                                    type="password" 
                                    id="registerPassword" 
                                    name="password" 
                                    className="form-control" 
                                    value={password}
                                    onChange={onChangeHandler}
                                />
                                <label className="form-label" htmlFor="registerPassword">Password</label>
                            </div>

                            <button type="submit" className="btn btn-primary btn-block mb-3">Sign up</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginForm;
