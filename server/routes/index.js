const express = require("express");
const router = express.Router();

router.get("/", (req, res, next) => {
    res.send({ response: "I am alive, for now" }).status(200);
    next()
});

module.exports = router;