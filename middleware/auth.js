const jwt = require('jsonwebtoken');
const pool = require('../db');
require('dotenv').config();

// Middleware function to verify JWT token
const verifyToken = async (req, res, next) => {
  // Extract the token from the Authorization header
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1]; // Bearer <token>

  // If no token exists, return a 403 Forbidden error
  if (!token) {
    return res.status(403).json({ error: 'Access token is required' });
  }

  try {
    // Verify the token using the JWT_SECRET
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    
    // Check if the user's password has been changed since the token was issued
    const userResult = await pool.query(
      'SELECT password_last_changed FROM users WHERE id = $1',
      [decoded.userId]
    );

    if (userResult.rows.length === 0) {
      return res.status(401).json({ error: 'User not found' });
    }

    const passwordLastChanged = userResult.rows[0].password_last_changed;
    
    // Check if the token was issued before the password was last changed
    const tokenIssuedAt = decoded.iat * 1000; // Convert to milliseconds
    const passwordChangedAt = new Date(passwordLastChanged).getTime();
    
    if (tokenIssuedAt < passwordChangedAt) {
      return res.status(401).json({ error: 'Token has been invalidated due to password change' });
    }

    // If valid, extract the userId from the token payload and attach it to the request object
    req.userId = decoded.userId;
    next();
  } catch (err) {
    // If invalid, return a 401 Unauthorized error
    if (err.name === 'TokenExpiredError' || err.name === 'JsonWebTokenError') {
      return res.status(401).json({ error: 'Invalid or expired token' });
    }
    
    console.error(err);
    return res.status(500).json({ error: 'Server error' });
  }
};

module.exports = { verifyToken };