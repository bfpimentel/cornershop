import express, { Request, Response } from "express";
import * as counterService from "./counter.service";
import { Counter, Sync } from "./counter.interface";

export const countersRouter = express.Router();

countersRouter.get("/", async (req: Request, res: Response) => {
  try {
    const counters: Counter[] = await counterService.getAll();

    res.status(200).send(counters);
  } catch (error) {
    res.status(500).send(error.message);
  }
});

countersRouter.put("/sync", async (req: Request, res: Response) => {
  try {
    const sync: Sync = req.body;
    const syncedCouters: Counter[] = await counterService.sync(sync);

    res.status(200).send(syncedCouters);
  } catch (error) {
    res.status(500).send(error.message);
  }
});
