const express = require('express');
const pool = require('../db');
const { verifyToken } = require('../middleware/auth');

const router = express.Router();

// Apply the verifyToken middleware to all routes in this file
router.use(verifyToken);

// POST /api/global-medicines - Create a new global medicine entry
router.post('/', async (req, res) => {
  try {
    const {
      name,
      brand_name,
      generic_name,
      dosage_form,
      strength,
      manufacturer,
      description,
      indications,
      contraindications,
      side_effects,
      warnings,
      interactions,
      storage_instructions,
      category,
      atc_code,
      fda_approval_date
    } = req.body;
    
    // Validate required fields
    if (!name) {
      return res.status(400).json({ error: 'Name is required' });
    }
    
    // Process array fields - ensure they're proper arrays for PostgreSQL
    const processArrayField = (field) => {
      if (Array.isArray(field)) {
        return field;
      } else if (typeof field === 'string') {
        try {
          const parsed = JSON.parse(field);
          return Array.isArray(parsed) ? parsed : [];
        } catch (parseError) {
          // If it's a string but not valid JSON, treat it as a single-item array
          return [field];
        }
      }
      return [];
    };
    
    const processedIndications = processArrayField(indications);
    const processedContraindications = processArrayField(contraindications);
    const processedSideEffects = processArrayField(side_effects);
    const processedWarnings = processArrayField(warnings);
    const processedInteractions = processArrayField(interactions);
    
    const newGlobalMedicine = await pool.query(
      `INSERT INTO global_medicines
       (name, brand_name, generic_name, dosage_form, strength, manufacturer, description,
        indications, contraindications, side_effects, warnings, interactions,
        storage_instructions, category, atc_code, fda_approval_date)
       VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16)
       RETURNING *`,
      [name, brand_name, generic_name, dosage_form, strength, manufacturer, description,
       processedIndications, processedContraindications, processedSideEffects, processedWarnings, processedInteractions,
       storage_instructions, category, atc_code, fda_approval_date]
    );
    
    res.status(201).json(newGlobalMedicine.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// GET /api/global-medicines - Retrieve all global medicines
router.get('/', async (req, res) => {
  try {
    const globalMedicines = await pool.query(
      'SELECT * FROM global_medicines ORDER BY name ASC'
    );
    
    res.status(200).json(globalMedicines.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// GET /api/global-medicines/:id - Retrieve a single global medicine by id
router.get('/:id', async (req, res) => {
  try {
    const { id } = req.params;
    
    const globalMedicine = await pool.query(
      'SELECT * FROM global_medicines WHERE id = $1',
      [id]
    );
    
    if (globalMedicine.rows.length === 0) {
      return res.status(404).json({ error: 'Global medicine not found' });
    }
    
    res.status(200).json(globalMedicine.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// PUT /api/global-medicines/:id - Update a global medicine by id
router.put('/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const {
      name,
      brand_name,
      generic_name,
      dosage_form,
      strength,
      manufacturer,
      description,
      indications,
      contraindications,
      side_effects,
      warnings,
      interactions,
      storage_instructions,
      category,
      atc_code,
      fda_approval_date
    } = req.body;
    
    // Validate required fields
    if (!name) {
      return res.status(400).json({ error: 'Name is required' });
    }
    
    // Process array fields - ensure they're proper arrays for PostgreSQL
    const processArrayField = (field) => {
      if (Array.isArray(field)) {
        return field;
      } else if (typeof field === 'string') {
        try {
          const parsed = JSON.parse(field);
          return Array.isArray(parsed) ? parsed : [];
        } catch (parseError) {
          // If it's a string but not valid JSON, treat it as a single-item array
          return [field];
        }
      }
      return [];
    };
    
    const processedIndications = processArrayField(indications);
    const processedContraindications = processArrayField(contraindications);
    const processedSideEffects = processArrayField(side_effects);
    const processedWarnings = processArrayField(warnings);
    const processedInteractions = processArrayField(interactions);
    
    const updatedGlobalMedicine = await pool.query(
      `UPDATE global_medicines
       SET name = $1, brand_name = $2, generic_name = $3, dosage_form = $4, strength = $5,
           manufacturer = $6, description = $7, indications = $8, contraindications = $9,
           side_effects = $10, warnings = $11, interactions = $12, storage_instructions = $13,
           category = $14, atc_code = $15, fda_approval_date = $16, updated_at = NOW()
       WHERE id = $17
       RETURNING *`,
      [name, brand_name, generic_name, dosage_form, strength, manufacturer, description,
       processedIndications, processedContraindications, processedSideEffects, processedWarnings, processedInteractions,
       storage_instructions, category, atc_code, fda_approval_date, id]
    );
    
    if (updatedGlobalMedicine.rows.length === 0) {
      return res.status(404).json({ error: 'Global medicine not found' });
    }
    
    res.status(200).json(updatedGlobalMedicine.rows[0]);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

// DELETE /api/global-medicines/:id - Delete a global medicine by id
router.delete('/:id', async (req, res) => {
  try {
    const { id } = req.params;
    
    const deletedGlobalMedicine = await pool.query(
      'DELETE FROM global_medicines WHERE id = $1 RETURNING *',
      [id]
    );
    
    if (deletedGlobalMedicine.rows.length === 0) {
      return res.status(404).json({ error: 'Global medicine not found' });
    }
    
    res.status(200).json({ message: 'Global medicine deleted successfully' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Server error' });
  }
});

module.exports = router;