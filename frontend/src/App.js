import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

const API = "http://localhost:8080/api/students";

function App() {
  const [students, setStudents] = useState([]);
  const [stats, setStats] = useState({});
  const [showForm, setShowForm] = useState(false);
  const [newStudent, setNewStudent] = useState({
    name: '', rollNo: '', studentClass: '', totalFees: ''
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const studentsRes = await axios.get(API);
      const statsRes = await axios.get(API + "/stats");
      setStudents(studentsRes.data);
      setStats(statsRes.data);
    } catch (error) {
      console.log("Backend not running");
    }
  };

  const addStudent = async () => {
    try {
      await axios.post(API, newStudent);
      setShowForm(false);
      setNewStudent({ name: '', rollNo: '', studentClass: '', totalFees: '' });
      fetchData();
    } catch (error) {
      alert("Error adding student");
    }
  };

  const deleteStudent = async (id) => {
    if (window.confirm('Delete?')) {
      await axios.delete(`${API}/${id}`);
      fetchData();
    }
  };

  const makePayment = async (id, dueAmount) => {
    const amt = prompt(`Enter amount to pay (Due: ₹${dueAmount}):`, dueAmount);
    if (amt && !isNaN(amt) && amt > 0) {
      await axios.put(`${API}/pay/${id}`, { amount: parseFloat(amt) });
      fetchData();
    }
  };

  return (
    <div className="container mt-4">
      <h1 className="text-center mb-4 text-primary">💰 Student Fee Portal</h1>

      <div className="row mb-4">
        <div className="col-md-3">
          <div className="card text-white bg-primary">
            <div className="card-body">
              <h5>Total Students</h5>
              <h3>{stats.totalStudents || 0}</h3>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card text-white bg-success">
            <div className="card-body">
              <h5>Fully Paid</h5>
              <h3>{stats.paid || 0}</h3>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card text-white bg-warning">
            <div className="card-body">
              <h5>Partial Paid</h5>
              <h3>{stats.partial || 0}</h3>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card text-white bg-danger">
            <div className="card-body">
              <h5>Fully Due</h5>
              <h3>{stats.due || 0}</h3>
            </div>
          </div>
        </div>
      </div>

      <button className="btn btn-success mb-3" onClick={() => setShowForm(true)}>
        + Add New Student
      </button>

      {showForm && (
        <div className="card p-3 mb-3">
          <h5>Add New Student</h5>
          <div className="row">
            <div className="col-md-3">
              <input type="text" className="form-control" placeholder="Name"
                value={newStudent.name} onChange={(e) =>
                setNewStudent({...newStudent, name: e.target.value})} />
            </div>
            <div className="col-md-2">
              <input type="text" className="form-control" placeholder="Roll No"
                value={newStudent.rollNo} onChange={(e) =>
                setNewStudent({...newStudent, rollNo: e.target.value})} />
            </div>
            <div className="col-md-2">
              <input type="text" className="form-control" placeholder="Class"
                value={newStudent.studentClass} onChange={(e) =>
                setNewStudent({...newStudent, studentClass: e.target.value})} />
            </div>
            <div className="col-md-2">
              <input type="number" className="form-control" placeholder="Total Fees"
                value={newStudent.totalFees} onChange={(e) =>
                setNewStudent({...newStudent, totalFees: e.target.value})} />
            </div>
            <div className="col-md-3">
              <button className="btn btn-primary me-2" onClick={addStudent}>Save</button>
              <button className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      <table className="table table-bordered table-striped">
        <thead className="table-dark">
          <tr>
            <th>Roll No</th>
            <th>Name</th>
            <th>Class</th>
            <th>Total (₹)</th>
            <th>Paid (₹)</th>
            <th>Due (₹)</th>
            <th>Status</th>
            <th>Last Payment</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {students.map(s => (
            <tr key={s.id}>
              <td>{s.rollNo}</td>
              <td>{s.name}</td>
              <td>{s.studentClass}</td>
              <td>₹{s.totalFees}</td>
              <td>₹{s.paidFees || 0}</td>
              <td>₹{(s.totalFees - (s.paidFees || 0)).toFixed(2)}</td>
              <td>
                <span className={`badge bg-${
                  s.status === 'PAID' ? 'success' :
                  s.status === 'PARTIAL' ? 'warning' : 'danger'
                }`}>
                  {s.status || 'DUE'}
                </span>
              </td>
              <td>{s.lastPaymentDate || 'No payment'}</td>
              <td>
                <button className="btn btn-sm btn-success me-2"
                  onClick={() => makePayment(s.id, s.totalFees - (s.paidFees || 0))}>
                  Pay
                </button>
                <button className="btn btn-sm btn-danger"
                  onClick={() => deleteStudent(s.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {students.length === 0 && (
        <div className="alert alert-info text-center">
          No students yet. Click "Add New Student" to get started!
        </div>
      )}
    </div>
  );
}

export default App;