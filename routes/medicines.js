const express = require('express');
const pool = require('../db');
const { verifyToken } = require('../middleware/auth');
const { cloudinary, upload } = require('../services/upload');

const router = express.Router();

// Apply the verifyToken middleware to all routes in this file
router.use(verifyToken);

// POST / - Create a new medicine entry linked to the req.userId
// This endpoint is being deprecated in favor of the profile-centric approach
router.post('/', async (req, res) => {
  try {
    console.log('Request Body:', JSON.stringify(req.body, null, 2));
    const { name, dosage, quantity, expiry_date, category, notes, image_url, profile_id, composition, form } = req.body;
    
    // Validate required fields
    if (!profile_id) {
      return res.status(400).json({ error: 'Profile ID is required' });
    }
    
    // Verify that the profile belongs to the authenticated user
    const profileResult = await pool.query(
      'SELECT id FROM profiles WHERE id = $1 AND user_id = $2',
      [profile_id, req.userId]
    );
    
    if (profileResult.rows.length === 0) {
      return res.status(403).json({ error: 'Profile not found or unauthorized' });
    }
    
    // Process composition field - ensure it's a proper JSON array
    let processedComposition = [];
    if (composition) {
      console.log('Raw composition:', composition, 'Type:', typeof composition);
      if (Array.isArray(composition)) {
        processedComposition = composition;
        console.log('Composition is already an array:', processedComposition);
      } else if (typeof composition === 'string') {
        try {
          // First check if it's already valid JSON
          const parsed = JSON.parse(composition);
          console.log('Parsed composition from string:', parsed);
          processedComposition = parsed;
        } catch (parseError) {
          console.log('Failed to parse composition as JSON:', parseError);
          // If it's a string but not valid JSON, treat it as a single-item array
          processedComposition = [composition];
        }
      } else {
        console.log('Composition is neither array nor string, converting to array');
        // For objects or other types, wrap in an array
        processedComposition = [composition];
      }
    }
    console.log('Processed Composition:', JSON.stringify(processedComposition));
    
    const newMedicine = await pool.query(
      `INSERT INTO user_medicines
       (user_id, profile_id, name, dosage, quantity, expiry_date, category, notes, image_url, composition, form, status)
       VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
       RETURNING *`,
      [req.userId, profile_id, name, dosage, quantity, expiry_date, category, notes, image_url, JSON.stringify(processedComposition), form || null, 'active']
    );
    
    res.status(201).json(newMedicine.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
 }
});

// POST /profiles/:profileId/medicines - Create a new medicine for a specific profile
router.post('/profiles/:profileId/medicines', async (req, res) => {
  try {
    const { profileId } = req.params;
    const { name, dosage, quantity, expiry_date, category, notes, image_url, composition, form } = req.body;
    
    // Validate that the profileId from the URL belongs to the authenticated user
    const profileResult = await pool.query(
      'SELECT id FROM profiles WHERE id = $1 AND user_id = $2',
      [profileId, req.userId]
    );
    
    if (profileResult.rows.length === 0) {
      return res.status(403).json({ error: 'Profile not found or unauthorized' });
    }
    
    // Process composition field - ensure it's a proper JSON array
    let processedComposition = [];
    if (composition) {
      console.log('Raw composition:', composition, 'Type:', typeof composition);
      if (Array.isArray(composition)) {
        processedComposition = composition;
        console.log('Composition is already an array:', processedComposition);
      } else if (typeof composition === 'string') {
        try {
          // First check if it's already valid JSON
          const parsed = JSON.parse(composition);
          console.log('Parsed composition from string:', parsed);
          processedComposition = parsed;
        } catch (parseError) {
          console.log('Failed to parse composition as JSON:', parseError);
          // If it's a string but not valid JSON, treat it as a single-item array
          processedComposition = [composition];
        }
      } else {
        console.log('Composition is neither array nor string, converting to array');
        // For objects or other types, wrap in an array
        processedComposition = [composition];
      }
    }
    console.log('Processed Composition:', JSON.stringify(processedComposition));
    
    // Create the new medicine entry, setting both user_id and profile_id with default status 'active'
    const newMedicine = await pool.query(
      `INSERT INTO user_medicines
       (user_id, profile_id, name, dosage, quantity, expiry_date, category, notes, image_url, composition, form, status)
       VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
       RETURNING *`,
      [req.userId, profileId, name, dosage, quantity, expiry_date, category, notes, image_url, JSON.stringify(processedComposition), form || null, 'active']
    );
    
    res.status(201).json(newMedicine.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// GET /profiles/:profileId/medicines - Retrieve all medicines for a specific profile
router.get('/profiles/:profileId/medicines', async (req, res) => {
  try {
    const { profileId } = req.params;
    
    // Validate that the profile belongs to the authenticated user
    const profileResult = await pool.query(
      'SELECT id FROM profiles WHERE id = $1 AND user_id = $2',
      [profileId, req.userId]
    );
    
    if (profileResult.rows.length === 0) {
      return res.status(403).json({ error: 'Profile not found or unauthorized' });
    }
    
    // Retrieve all active medicines for the specified profileId
    const medicines = await pool.query(
      'SELECT * FROM user_medicines WHERE profile_id = $1 AND status = $2 ORDER BY created_at DESC',
      [profileId, 'active']
    );
    
    res.status(200).json(medicines.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// GET / - Retrieve all active medicines where user_id matches req.userId
router.get('/', async (req, res) => {
  try {
    const medicines = await pool.query(
      'SELECT * FROM user_medicines WHERE user_id = $1 AND status = $2 ORDER BY created_at DESC',
      [req.userId, 'active']
    );
    
    res.status(200).json(medicines.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// GET /:id - Retrieve a single active medicine by id, ensuring it belongs to req.userId
router.get('/:id', async (req, res) => {
  try {
    const { id } = req.params;
    
    const medicine = await pool.query(
      'SELECT * FROM user_medicines WHERE id = $1 AND user_id = $2 AND status = $3',
      [id, req.userId, 'active']
    );
    
    if (medicine.rows.length === 0) {
      return res.status(404).json({ error: 'Medicine not found' });
    }
    
    res.status(200).json(medicine.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// PUT /:id - Update an active medicine by id, ensuring it belongs to the specified profile
router.put('/:id', async (req, res) => {
  try {
    console.log('Request Body:', JSON.stringify(req.body, null, 2));
    const { id } = req.params;
    const { name, dosage, quantity, expiry_date, category, notes, image_url, profile_id, composition, form } = req.body;
    
    // Validate required fields
    if (!profile_id) {
      return res.status(400).json({ error: 'Profile ID is required' });
    }
    
    // Verify that the profile belongs to the authenticated user
    const profileResult = await pool.query(
      'SELECT id FROM profiles WHERE id = $1 AND user_id = $2',
      [profile_id, req.userId]
    );
    
    if (profileResult.rows.length === 0) {
      return res.status(403).json({ error: 'Profile not found or unauthorized' });
    }
    
    // Verify that the medicine exists, belongs to the user, and is associated with the specified profile
    const medicineResult = await pool.query(
      'SELECT id FROM user_medicines WHERE id = $1 AND user_id = $2 AND profile_id = $3 AND status = $4',
      [id, req.userId, profile_id, 'active']
    );
    
    if (medicineResult.rows.length === 0) {
      return res.status(404).json({ error: 'Medicine not found or unauthorized' });
    }
    
    // Process composition field - ensure it's a proper JSON array
    let processedComposition = [];
    if (composition) {
      console.log('Raw composition:', composition, 'Type:', typeof composition);
      if (Array.isArray(composition)) {
        processedComposition = composition;
        console.log('Composition is already an array:', processedComposition);
      } else if (typeof composition === 'string') {
        try {
          // First check if it's already valid JSON
          const parsed = JSON.parse(composition);
          console.log('Parsed composition from string:', parsed);
          processedComposition = parsed;
        } catch (parseError) {
          console.log('Failed to parse composition as JSON:', parseError);
          // If it's a string but not valid JSON, treat it as a single-item array
          processedComposition = [composition];
        }
      } else {
        console.log('Composition is neither array nor string, converting to array');
        // For objects or other types, wrap in an array
        processedComposition = [composition];
      }
    }
    console.log('Processed Composition:', JSON.stringify(processedComposition));
    
    const updatedMedicine = await pool.query(
      `UPDATE user_medicines
       SET name = $1, dosage = $2, quantity = $3, expiry_date = $4,
           category = $5, notes = $6, image_url = $7, composition = $8, form = $9, updated_at = NOW()
       WHERE id = $10 AND user_id = $11 AND profile_id = $12 AND status = $13
       RETURNING *`,
      [name, dosage, quantity, expiry_date, category, notes, image_url, JSON.stringify(processedComposition), form || null, id, req.userId, profile_id, 'active']
    );
    
    res.status(200).json(updatedMedicine.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// DELETE /:id - Soft delete a medicine by id, ensuring it belongs to req.userId
router.delete('/:id', async (req, res) => {
  try {
    const { id } = req.params;
    
    // Soft delete by updating the status to 'inactive'
    const updatedMedicine = await pool.query(
      'UPDATE user_medicines SET status = $1, updated_at = NOW() WHERE id = $2 AND user_id = $3 RETURNING *',
      ['inactive', id, req.userId]
    );
    
    if (updatedMedicine.rows.length === 0) {
      return res.status(404).json({ error: 'Medicine not found' });
    }
    
    res.status(200).json({ message: 'Medicine deleted successfully' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// POST /:id/takedose - Decrement the quantity of an active medicine by 1 where the id and user_id match
router.post('/:id/takedose', async (req, res) => {
  try {
    const { id } = req.params;
    
    // First, check if the active medicine exists and belongs to the user
    const medicine = await pool.query(
      'SELECT quantity FROM user_medicines WHERE id = $1 AND user_id = $2 AND status = $3',
      [id, req.userId, 'active']
    );
    
    if (medicine.rows.length === 0) {
      return res.status(404).json({ error: 'Medicine not found' });
    }
    
    // Check if quantity is already 0
    if (medicine.rows[0].quantity <= 0) {
      return res.status(400).json({ error: 'Medicine quantity is already 0' });
    }
    
    // Decrement the quantity by 1
    const updatedMedicine = await pool.query(
      'UPDATE user_medicines SET quantity = quantity - 1, updated_at = NOW() WHERE id = $1 AND user_id = $2 AND status = $3 RETURNING *',
      [id, req.userId, 'active']
    );
    
    res.status(200).json(updatedMedicine.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// POST /upload-image - Upload medicine image to Cloudinary
router.post('/upload-image', upload.single('medicineImage'), async (req, res) => {
  try {
    if (!req.file) {
      return res.status(400).json({ error: 'No file uploaded' });
    }
    
    // Upload the file to Cloudinary
    const result = await new Promise((resolve, reject) => {
      const uploadStream = cloudinary.uploader.upload_stream(
        { folder: 'medicine_tracker' },
        (error, result) => {
          if (error) {
            reject(error);
          } else {
            resolve(result);
          }
        }
      );
      
      // Stream the file buffer to Cloudinary
      uploadStream.end(req.file.buffer);
    });
    
    // Return the secure_url from the Cloudinary response
    res.status(200).json({ imageUrl: result.secure_url });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

module.exports = router;