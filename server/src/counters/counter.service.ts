import { Counter, Sync } from "./counter.interface";

let counters: Counter[] = [];

export const getAll = async (): Promise<Counter[]> => counters;

export const sync = async (sync: Sync): Promise<Counter[]> => {
  Array.from(sync.deletedCounterIds).forEach((id) => {
    const index = counters.findIndex((counter) => counter.id == id);
    const counterToBeDeleted = counters[index];
    counters.splice(index, 1);
    console.log(`SYNC: Deleted counter ${counterToBeDeleted}`);
  });

  Array.from(sync.counters).forEach((newCounter) => {
    const index = counters.findIndex((counter) => counter.id == newCounter.id);

    if (index == -1) {
      counters.push(newCounter);
      console.log(`SYNC: Added new counter ${newCounter}`);
    } else {
      counters[index] = newCounter;
      console.log(`SYNC: Updated counter ${newCounter}`);
    }
  });

  return getAll();
};
