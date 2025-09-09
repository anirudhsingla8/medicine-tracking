const express = require('express');
const pool = require('../db');
const { verifyToken } = require('../middleware/auth');

const router = express.Router();

// Apply the verifyToken middleware to all routes in this file
router.use(verifyToken);

// POST /medicines/:medicineId/schedules - Create a new schedule for a specific medicine
router.post('/medicines/:medicineId/schedules', async (req, res) => {
  try {
    const { medicineId } = req.params;
    const { time_of_day, frequency, is_active } = req.body;

    // Validate required fields
    if (!time_of_day) {
      return res.status(400).json({ error: 'Time of day is required' });
    }

    // Logic must first retrieve the medicine by :medicineId to verify it belongs to req.userId and to get its profile_id
    const medicineResult = await pool.query(
      'SELECT id, profile_id FROM user_medicines WHERE id = $1 AND user_id = $2',
      [medicineId, req.userId]
    );

    if (medicineResult.rows.length === 0) {
      return res.status(404).json({ error: 'Medicine not found or unauthorized' });
    }

    const medicine = medicineResult.rows[0];
    const profileId = medicine.profile_id;

    // Insert the new schedule, populating medicine_id, profile_id, and user_id
    const newSchedule = await pool.query(
      `INSERT INTO schedules 
       (medicine_id, profile_id, user_id, time_of_day, frequency, is_active)
       VALUES ($1, $2, $3, $4, $5, $6)
       RETURNING *`,
      [medicineId, profileId, req.userId, time_of_day, frequency || 'daily', is_active !== undefined ? is_active : true]
    );

    res.status(201).json(newSchedule.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// GET /medicines/:medicineId/schedules - Retrieve all schedules for a medicine
router.get('/medicines/:medicineId/schedules', async (req, res) => {
 try {
    const { medicineId } = req.params;

    // Validate ownership first by checking the medicine belongs to req.userId
    const medicineResult = await pool.query(
      'SELECT id FROM user_medicines WHERE id = $1 AND user_id = $2',
      [medicineId, req.userId]
    );

    if (medicineResult.rows.length === 0) {
      return res.status(404).json({ error: 'Medicine not found or unauthorized' });
    }

    // Retrieve all schedules for a medicine
    const schedules = await pool.query(
      'SELECT * FROM schedules WHERE medicine_id = $1 ORDER BY time_of_day',
      [medicineId]
    );

    res.status(200).json(schedules.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// PUT /schedules/:scheduleId - Update a schedule's details
router.put('/schedules/:scheduleId', async (req, res) => {
  try {
    const { scheduleId } = req.params;
    const { time_of_day, frequency, is_active } = req.body;

    // Validate ownership by checking the user_id on the schedule record itself
    const scheduleResult = await pool.query(
      'SELECT id FROM schedules WHERE id = $1 AND user_id = $2',
      [scheduleId, req.userId]
    );

    if (scheduleResult.rows.length === 0) {
      return res.status(404).json({ error: 'Schedule not found or unauthorized' });
    }

    // Update the schedule
    const updatedSchedule = await pool.query(
      `UPDATE schedules 
       SET time_of_day = COALESCE($1, time_of_day), 
           frequency = COALESCE($2, frequency), 
           is_active = COALESCE($3, is_active)
       WHERE id = $4 AND user_id = $5
       RETURNING *`,
      [time_of_day, frequency, is_active, scheduleId, req.userId]
    );

    res.status(200).json(updatedSchedule.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// DELETE /schedules/:scheduleId - Delete a schedule
router.delete('/schedules/:scheduleId', async (req, res) => {
  try {
    const { scheduleId } = req.params;

    // Validate ownership via the user_id on the schedule record
    const scheduleResult = await pool.query(
      'SELECT id FROM schedules WHERE id = $1 AND user_id = $2',
      [scheduleId, req.userId]
    );

    if (scheduleResult.rows.length === 0) {
      return res.status(404).json({ error: 'Schedule not found or unauthorized' });
    }

    // Delete the schedule
    await pool.query(
      'DELETE FROM schedules WHERE id = $1 AND user_id = $2',
      [scheduleId, req.userId]
    );

    res.status(200).json({ message: 'Schedule deleted successfully' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

module.exports = router;