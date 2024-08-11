
import { request, setAuthHeader } from './helpers/axios_helper';


const AuthContent = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await request("GET", "/messages", {});
                setData(response.data);
            } catch (error) {
                if (error.response.status === 401) {
                    setAuthHeader(null);
                } else {
                    setData([error.response.code]); // Zmieniono na tablicę, aby była zgodna z oczekiwanym formatem
                }
            }
        };

        fetchData();
    }, []); // Pusta tablica zależności oznacza, że efekt wykona się tylko raz po zamontowaniu komponentu

    return (
        <div className="row justify-content-md-center">
            <div className="col-4">
                <div className="card" style={{width: "18rem"}}>
                    <div className="card-body">
                        <h5 className="card-title">Backend response</h5>
                        <p className="card-text">Content:</p>
                        <ul>
                            {data && data.map((line, index) => (
                                <li key={index}>{line}</li>
                            ))}
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AuthContent;