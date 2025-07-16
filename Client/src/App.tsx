import './App.css'

function App() {

  const baseUrl = 'http://localhost:9090';


  const login = async () => {
    
    const response = await fetch(`${baseUrl}/auth/login`, {
      credentials: 'include',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: "user",
        password: "password"
      })
    });

    const data = await response.json();
    console.log(data);
  }

  const getResource = async () => {
    
    const response = await fetch(`${baseUrl}/resource`, {
      credentials: 'include',
      method: 'GET'
    });

    const data = await response.json();
    console.log(data);
  }

  const postResource = async () => {
    
    const response = await fetch(`${baseUrl}/resource`, {
      credentials: 'include',
      method: 'POST'
    });

    const data = await response.json();
    console.log(data);
  }

  return (
    <>
      <div>
        <button onClick={login}>Login</button>
        <button onClick={getResource}>GET</button>
        <button onClick={postResource}>POST</button>
      </div>
    </>
  )
}

export default App