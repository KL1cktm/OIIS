import cv2
import torch
import os
import numpy as np

model_path = r'D:\uni\5 sem\OIIS\OIIS_6\yolov5\yolov5l.pt'
model = torch.hub.load('ultralytics/yolov5', 'custom', path=model_path, verbose=False)
print("Model loaded successfully.")

with open('coco.txt', 'r') as f:
    class_labels = f.read().strip().split('\n')

orange_img = cv2.imread('orange.png', cv2.IMREAD_UNCHANGED)
if orange_img.shape[2] == 3:
    raise ValueError("Загруженное изображение не содержит альфа-канал. Убедитесь, что изображение имеет прозрачность.")


def predict(model, class_labels, img):
    labels_to_detect = ['car', 'bicycle', 'bus', 'truck', 'motorbike', 'apple', 'laptop', 'mouse', 'tvmonitor',
                        'cell phone', 'keyboard']
    total_counts = {label: 0 for label in labels_to_detect}
    results = model(img)

    for obj in results.pred[0]:
        label_index = int(obj[-1])
        label = class_labels[label_index]
        if label in labels_to_detect:
            total_counts[label] += 1
            bbox = obj[:4].cpu().numpy().astype(int)
            cv2.rectangle(img, (bbox[0], bbox[1]), (bbox[2], bbox[3]), (0, 255, 0), 2)
            cv2.putText(img, label, (bbox[0], bbox[1] - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)

    return img, total_counts


def predict_and_replace(model, class_labels, img, replacement_img, target_class, replacement_class, padding=10):
    results = model(img)

    for obj in results.pred[0]:
        class_index = int(obj[-1])
        label = class_labels[class_index]

        if label == target_class:
            bbox = obj[:4].cpu().numpy().astype(int)
            x1, y1, x2, y2 = bbox

            x1, y1 = max(0, x1 - padding), max(0, y1 - padding)
            x2, y2 = min(img.shape[1], x2 + padding), min(img.shape[0], y2 + padding)

            replacement_resized = cv2.resize(replacement_img[:, :, :3], (x2 - x1, y2 - y1))
            alpha_resized = cv2.resize(replacement_img[:, :, 3], (x2 - x1, y2 - y1))

            mask = alpha_resized / 255.0
            inverse_mask = 1 - mask

            for c in range(3):
                img[y1:y2, x1:x2, c] = (replacement_resized[:, :, c] * mask + img[y1:y2, x1:x2, c] * inverse_mask)

            # cv2.putText(img, replacement_class, (x1, y1 - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
    return img


def main():
    print("Choose an option:")
    print("1. Find objects on the image")
    print("2. Find and replace an object on the image")
    choice = input("Enter 1 or 2: ")

    if choice == '1':
        for image_file in os.listdir("images"):
            image_path = os.path.join("images", image_file)
            img = cv2.imread(image_path)
            img, total_counts = predict(model, class_labels, img)

            for label, count in total_counts.items():
                print(f"Total {label}s: {count}")

            cv2.imshow('Detection Results', img)
            cv2.waitKey(0)
            cv2.destroyAllWindows()

    elif choice == '2':

        for image_file in os.listdir("images"):
            image_path = os.path.join("images", image_file)
            img = cv2.imread(image_path)

            modified_img = predict_and_replace(model, class_labels, img, orange_img, 'apple', 'orange', padding=20)

            cv2.imshow('Modified Image', modified_img)
            cv2.imwrite(f'images_output/modified_{image_file}', modified_img)
            cv2.waitKey(0)
            cv2.destroyAllWindows()


if __name__ == "__main__":
    main()
