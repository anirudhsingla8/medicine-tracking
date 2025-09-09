const express = require('express');
const pool = require('../db');
const { verifyToken } = require('../middleware/auth');

const router = express.Router();

// Apply the verifyToken middleware to all routes in this file
router.use(verifyToken);

// POST /api/profiles - Create a new profile
router.post('/', async (req, res) => {
  try {
    const { name } = req.body;

    // Validate required fields
    if (!name) {
      return res.status(400).json({ error: 'Name is required' });
    }

    // Create a new profile linked to the authenticated user's ID
    const newProfile = await pool.query(
      'INSERT INTO profiles (user_id, name) VALUES ($1, $2) RETURNING *',
      [req.userId, name]
    );

    // Return the new profile object with a 201 status
    res.status(201).json(newProfile.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// GET /api/profiles - Retrieve all profiles for the authenticated user
router.get('/', async (req, res) => {
  try {
    // Retrieve all profiles where user_id matches req.userId
    const profiles = await pool.query(
      'SELECT * FROM profiles WHERE user_id = $1 ORDER BY created_at DESC',
      [req.userId]
    );

    // Return an array of profile objects
    res.status(200).json(profiles.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// PUT /api/profiles/:profileId - Update a profile
router.put('/:profileId', async (req, res) => {
  try {
    const { profileId } = req.params;
    const { name } = req.body;

    // Validate required fields
    if (!name) {
      return res.status(400).json({ error: 'Name is required' });
    }

    // Update the profile specified by :profileId
    // The SQL query must include WHERE id = $1 AND user_id = $2 to ensure the authenticated user owns the profile
    const updatedProfile = await pool.query(
      'UPDATE profiles SET name = $1 WHERE id = $2 AND user_id = $3 RETURNING *',
      [name, profileId, req.userId]
    );

    // Check if profile was found and updated
    if (updatedProfile.rows.length === 0) {
      return res.status(404).json({ error: 'Profile not found or unauthorized' });
    }

    // Return the updated profile object
    res.status(200).json(updatedProfile.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// DELETE /api/profiles/:profileId - Delete a profile
router.delete('/:profileId', async (req, res) => {
  try {
    const { profileId } = req.params;

    // Delete the profile specified by :profileId
    // The SQL query must include WHERE id = $1 AND user_id = $2 to ensure ownership
    const deletedProfile = await pool.query(
      'DELETE FROM profiles WHERE id = $1 AND user_id = $2 RETURNING *',
      [profileId, req.userId]
    );

    // Check if profile was found and deleted
    if (deletedProfile.rows.length === 0) {
      return res.status(404).json({ error: 'Profile not found or unauthorized' });
    }

    // Return a success message
    res.status(200).json({ message: 'Profile deleted successfully' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

module.exports = router;