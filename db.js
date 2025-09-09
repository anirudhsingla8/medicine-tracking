const { Pool } = require('pg');
require('dotenv').config();

// Create a new Pool instance configured with environment variables
const pool = new Pool({
  host: process.env.DB_HOST,
  port: process.env.DB_PORT,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  ssl: process.env.DB_SSL_MODE ? {
    mode: process.env.DB_SSL_MODE,
    rejectUnauthorized: false // For development only, should be true in production with proper certificates
  } : false
});

module.exports = pool;