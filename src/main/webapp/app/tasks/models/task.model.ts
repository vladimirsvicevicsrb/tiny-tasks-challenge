import { TaskFile } from "./task-file.model";

/**
 * A tiny task.
 */
export interface Task {
  id: string;
  name: string;
  dueDate?: string;
  files?: TaskFile[];
}
