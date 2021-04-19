export interface Counter {
  id: string;
  title: string;
  count: number;
}

export interface Sync {
  deletedCounterIds: string[];
  counters: Counter[];
}
