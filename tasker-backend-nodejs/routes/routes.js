

var appRouter = function(app) {

    app.get("/task/list", function(req, res) {
        req.db.get('task').find({},{},function(e, docs) {
           res.json(docs);
        });

    });

    app.post("/task", function (req, res) {
        console.log(req);
    });

    app.post("/sync", function (req, res) {
        console.log(req);
    });
};

module.exports = appRouter;